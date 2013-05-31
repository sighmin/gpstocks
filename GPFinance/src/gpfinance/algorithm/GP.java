package gpfinance.algorithm;

import gpfinance.algorithm.strategies.InitializationStrategy;
import gpfinance.algorithm.strategies.RankBasedSelectionStrategy;
import gpfinance.algorithm.strategies.RandomSelectionStrategy;
import gpfinance.algorithm.strategies.StochasticMuLambdaSelectionStrategy;
import gpfinance.algorithm.strategies.TreeMutationStrategy;
import gpfinance.algorithm.strategies.MuLambdaSelectionStrategy;
import gpfinance.algorithm.strategies.SexualCrossoverStrategy;
import gpfinance.U;
import gpfinance.algorithm.interfaces.SelectionStrategy;
import gpfinance.algorithm.interfaces.MutationStrategy;
import gpfinance.algorithm.interfaces.CrossoverStrategy;
import gpfinance.datatypes.FitnessData;
import gpfinance.datatypes.Indicator;
import gpfinance.datatypes.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class GP {
    /* Financial Data */
    public static ArrayList<Security> securities = new ArrayList();

    /* Miscellaneous configuration */
    private static final int NUM_MUTATIONS = 6;
    private static final int RESOLUTION = 5;
    private static final String DELIMETER = "|";
    private static final int QUARTER_NUM = 1;
    private static final double SIZE_CONTRIBUTION = 0.2;
    private static final String[] FILE_PATH = {"/home/simon/Varsity/AI/assignments/assignment4/GPStocks/GPFinance/data/Fitness.csv", "/home/stuart/Documents/GPStocks/GPFinance/data/Fitness.csv"};
            
    /* Control Parameters */
    private char analysisType = 'F';
    private int generations = 2000;
    private int populationSize = 100;
    private ArrayList<Individual> population = new ArrayList(populationSize);
    /* Strategy control parameters */
    //                                      {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] initialMutationRates = {0.9,   0.0,   0.6,       0.5,  0.6,        0.9};
    //                                      {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] finalMutationRates =   {0.4,   0.4,   0.1,       0.1,  0.1,        0.4};
    private double initialCrossoverProb = 0.75;
    private double finalCrossoverProb = 0.25;
    private double[] restartRates = {0.6, 0.02};
    
    /* Strategies */
    private InitializationStrategy initializationStrategy = new InitializationStrategy(analysisType);
    private SelectionStrategy populationSelectionStrategy = new StochasticMuLambdaSelectionStrategy(restartRates); // elitism
    private SelectionStrategy reproductionSelectionStrategy = new RankBasedSelectionStrategy();
    private CrossoverStrategy crossoverStrategy = new SexualCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
    private MutationStrategy mutationStrategy = new TreeMutationStrategy(initialMutationRates, finalMutationRates);

    // generate constructors once all instance variables defined
    public GP() { /* Create GP with default parameters */ }

    public GP(HashMap options) {
        // Set class values if they exist in the hash
        if (options.containsKey("type")) {
            this.analysisType = (options.get("type") == "fundamental") ? 'F' : 'T';
        }
        if (options.containsKey("generations")) {
            this.generations = Integer.parseInt((String) options.get("generations"));
        }
        if (options.containsKey("population")) {
            this.populationSize = Integer.parseInt((String) options.get("population"));
        }
        if (options.containsKey("crossoverRate")) {
            String[] rates = ((String) options.get("crossoverRate")).split(":");
            this.initialCrossoverProb = Double.parseDouble(rates[0]);
            this.finalCrossoverProb = Double.parseDouble(rates[1]);
        }
        if (options.containsKey("mutationRateStart")) {
            String[] rates = ((String) options.get("mutationRateStart")).split(":");
            this.initialMutationRates = new double[NUM_MUTATIONS];
            for (int i = 0; i < rates.length; ++i) {
                initialMutationRates[i] = Double.parseDouble(rates[i]);
            }
        }
        if (options.containsKey("mutationRateEnd")) {
            String[] rates = ((String) options.get("mutationRateEnd")).split(":");
            this.finalMutationRates = new double[NUM_MUTATIONS];
            for (int i = 0; i < rates.length; ++i) {
                finalMutationRates[i] = Double.parseDouble(rates[i]);
            }
        }
        // Set class strategies if they exist in the hash
        if (options.containsKey("populationSelection")) {
            String tmp = ((String) options.get("populationSelection"));
            switch (tmp) {
                case "mulambda":
                    this.populationSelectionStrategy = new MuLambdaSelectionStrategy();
                    break;
                case "rankbased":
                    this.populationSelectionStrategy = new RankBasedSelectionStrategy();
                    break;
                default:
                    this.populationSelectionStrategy = new RandomSelectionStrategy();
                    break;
            }
        }
        if (options.containsKey("reproductionSelection")) {
            String tmp = ((String) options.get("reproductionSelection"));
            switch (tmp) {
                case "rankbased":
                    this.populationSelectionStrategy = new RankBasedSelectionStrategy();
                    break;
                case "mulambda":
                    this.populationSelectionStrategy = new MuLambdaSelectionStrategy();
                    break;
                default:
                    this.populationSelectionStrategy = new RandomSelectionStrategy();
                    break;
            }
        }

        // Create the rest of the variables and strategies
        population = new ArrayList(populationSize);
        initializationStrategy = new InitializationStrategy(analysisType);
        crossoverStrategy = new SexualCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
        mutationStrategy = new TreeMutationStrategy(initialMutationRates, finalMutationRates);
    }

    public void run() {
        // Initialize population & financial data
        Individual.SIZE_CONTRIBUTION = SIZE_CONTRIBUTION;
        Individual.fitnessData = new FitnessData(FILE_PATH, QUARTER_NUM);
        initializationStrategy.init(population, populationSize);
        
        // For each generation
        int gen = 0;
        do {
            double progress = ((double) gen / (double) generations);
            
            // Measure individuals
            measurePopulation(population, gen);

            // Clone previous generation P
            ArrayList<Individual> previousPopulation = new ArrayList();
            for (int i = 0; i < population.size(); ++i) {
                previousPopulation.add(population.get(i).clone());
            }

            // Selection for reproduction
            //TODO: Agree on selection here with Stu
            ArrayList<Individual> candidatePopulation = reproductionSelectionStrategy.select(population, population.size() / 2);

            // Reproduction producing P'
            ArrayList<Individual> crossoverOffspring = crossoverStrategy.crossover(candidatePopulation, progress);
            measurePopulation(crossoverOffspring, gen);

            // Mutation producing P''
            ArrayList<Individual> mutationOffspring = mutationStrategy.mutate(crossoverOffspring, progress);
            measurePopulation(mutationOffspring, gen);
            
            // Select P(t+1) from union of offspring: P U P'' -- should we select from P U P' U P'' ?
            previousPopulation.addAll(mutationOffspring);
            population = populationSelectionStrategy.selectDynamic(previousPopulation, populationSize, progress);

            // Advance to next generation
            if (gen % RESOLUTION == 0){
                printMeasurements(gen);
            }
            ++gen;
        } while (gen < generations);
        printMeasurements(gen);

        //U.m("\n\n****************************************  " + "RUN COMPLETE" + "  ****************************************\n\n");
        //printBestIndividual();
    }
    
    private void printMeasurements(int generation){
        Individual best = getBestIndividual();
        double fitness = best.getFitness();
        int size = best.size();
        double heterogenity = best.getHeterogeneity();
        Indicator mostOccuring = best.getMostOccuringIndicator();
        double fitnessSizeRatio = fitness / (double) size;
        U.m(generation + DELIMETER
                + fitness + DELIMETER
                + size + DELIMETER
                + fitnessSizeRatio + DELIMETER
                + heterogenity + DELIMETER
                + mostOccuring + ","
                + mostOccuring.getCode());
    }

    private void measurePopulation(ArrayList<Individual> individuals, int generation) {
        for (Individual individual : individuals) {
            individual.measure(generation, securities);
        }
    }
    
    private Individual getBestIndividual() {
        Collections.sort(population, Individual.DescendingFitness);
        return population.get(0);
    }

    private void printBestIndividual() {
        U.m("Best f(): " + getBestIndividual().getFitness() + ", fs(): " + (getBestIndividual().getFitness() + (1.0 / getBestIndividual().getTree().size())));
    }

    private void printPopulationFitnesses(ArrayList<Individual> individuals) {
        Collections.sort(individuals, Individual.DescendingFitness);
        for (Individual i : individuals) {
            U.p(i.getFitness() + ", ");
        }
        U.pl();
    }
}
