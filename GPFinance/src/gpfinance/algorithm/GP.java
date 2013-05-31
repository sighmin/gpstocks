package gpfinance.algorithm;

import gpfinance.algorithm.strategies.*;
import gpfinance.algorithm.interfaces.*;
import gpfinance.datatypes.*;
import gpfinance.U;
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
    private static int QUARTER_NUM = 3;
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
    private CrossoverStrategy crossoverStrategy = new SexualRootCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
    private MutationStrategy mutationStrategy = new TreeMutationStrategy(initialMutationRates, finalMutationRates);

    // generate constructors once all instance variables defined
    public GP() { /* Create GP with default parameters */ }

    public GP(HashMap options) {
        // Set class values if they exist in the hash
        if (options.containsKey("financialQuarter")){
            this.QUARTER_NUM = Integer.parseInt((String) options.get("financialQuarter"));
        }
        if (options.containsKey("type")) {
            String s = (String) options.get("type");
            this.analysisType = (options.get("type").equals("fundamental")) ? 'F' : 'T';
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
                this.initialMutationRates[i] = Double.parseDouble(rates[i]);
            }
        }
        if (options.containsKey("mutationRateEnd")) {
            String[] rates = ((String) options.get("mutationRateEnd")).split(":");
            this.finalMutationRates = new double[NUM_MUTATIONS];
            for (int i = 0; i < rates.length; ++i) {
                this.finalMutationRates[i] = Double.parseDouble(rates[i]);
            }
        }
        if (options.containsKey("restartRates")) {
            String[] rates = ((String) options.get("restartRates")).split(":");
            this.restartRates = new double[2];
            this.restartRates[0] = Double.parseDouble(rates[0]);
            this.restartRates[1] = Double.parseDouble(rates[1]);
        }
        // Set class strategies if they exist in the hash
        if (options.containsKey("populationSelection")) {
            String tmp = ((String) options.get("populationSelection"));
            switch (tmp) {
                case "smulambda":
                    this.populationSelectionStrategy = new StochasticMuLambdaSelectionStrategy(restartRates);
                    break;
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
                case "smulambda":
                    this.reproductionSelectionStrategy = new StochasticMuLambdaSelectionStrategy(restartRates);
                    break;
                case "rankbased":
                    this.reproductionSelectionStrategy = new RankBasedSelectionStrategy();
                    break;
                case "mulambda":
                    this.reproductionSelectionStrategy = new MuLambdaSelectionStrategy();
                    break;
                default:
                    this.reproductionSelectionStrategy = new RandomSelectionStrategy();
                    break;
            }
        }
        if (options.containsKey("crossoverStrategy")){
            String tmp = ((String) options.get("crossoverStrategy"));
            switch (tmp) {
                case "sexualRandOnePointCrossover":
                    this.crossoverStrategy = new SexualCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
                    break;
                case "sexualOnePointCrossover":
                    this.crossoverStrategy = new SexualRootCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
                    break;
            }
        }

        // Create the rest of the variables and strategies
        population = new ArrayList(populationSize);
        initializationStrategy = new InitializationStrategy(analysisType);
        mutationStrategy = new TreeMutationStrategy(initialMutationRates, finalMutationRates);
    }

    public void run() {
        // Initialize population & financial data
        Individual.SIZE_CONTRIBUTION = SIZE_CONTRIBUTION;
        Individual.fitnessData = new FitnessData(FILE_PATH, QUARTER_NUM);
        initializationStrategy.init(population, populationSize);
        printParameters();
        
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

    private void printParameters() {
        U.m("#########################################################");
        U.m("# Algorithm Parameters #");
        U.m("# Generations = " + generations);
        U.m("# Population size = " + populationSize);
        U.m("# Resolution = " + RESOLUTION);
        U.m("#########################################################");
        U.m("# Financial Parameters #");
        U.m("# Quarter = " + QUARTER_NUM);
        U.m("# Crossover probabilities = " + initialCrossoverProb + " -> " + finalCrossoverProb);
        U.mnl("# Mutation start probabilities = ");
        for (int i = 0; i < initialMutationRates.length; ++i) {
            if (i != initialMutationRates.length-1){
                U.mnl(initialMutationRates[i] + ",");
            } else {
                U.m(initialMutationRates[i]);
            }
        }
        U.mnl("# Mutation end probabilities = ");
        for (int i = 0; i < finalMutationRates.length; ++i) {
            if (i != finalMutationRates.length-1){
                U.mnl(finalMutationRates[i] + ",");
            } else {
                U.m(finalMutationRates[i]);
            }
        }
        U.m("#########################################################");
    }
}
