
package gpfinance.algorithm;

import gpfinance.U;
import gpfinance.algorithm.interfaces.SelectionStrategy;
import gpfinance.algorithm.interfaces.MutationStrategy;
import gpfinance.algorithm.interfaces.CrossoverStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class GP {
    private static final int numMutations = 6;
    /* Control Parameters */
    private int generations = 500;
    private int populationSize = 25;
    private ArrayList<Individual> population = new ArrayList(populationSize);
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] initialMutationRates = {1.0, 0.0,   0.75,       0.75,  0.75,        0.9};
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] finalMutationRates =   {0.5, 0.0,   0.2,       0.4,  0.2,        0.4};
    private double initialCrossoverProb = 0.8;
    private double finalCrossoverProb = 0.6;
    private char analysisType = 'F';
    
    /* Strategies */
    private InitializationStrategy initializationStrategy = new InitializationStrategy(analysisType);
    private SelectionStrategy populationSelectionStrategy = new MuLambdaSelectionStrategy();
    private SelectionStrategy reproductionSelectionStrategy = new RankBasedSelectionStrategy();
    private CrossoverStrategy crossoverStrategy = new SexualCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
    private MutationStrategy mutationStrategy = new TreeMutationStrategy(initialMutationRates, finalMutationRates);

    // generate constructors once all instance variables defined
    public GP(){ /* Create GP with default parameters */ }
    
    public GP(HashMap options){
        // Set class values if they exist in the hash
        if (options.containsKey("type"))
            this.analysisType = (options.get("type") == "fundamental") ? 'F' : 'T';
        if (options.containsKey("generations"))
            this.generations = Integer.parseInt((String)options.get("generations"));
        if (options.containsKey("population"))
            this.populationSize = Integer.parseInt((String)options.get("population"));
        if (options.containsKey("crossoverRate")){
            String[] rates = ((String)options.get("crossoverRate")).split(":");
            this.initialCrossoverProb = Double.parseDouble(rates[0]);
            this.finalCrossoverProb = Double.parseDouble(rates[1]);
        }
        if (options.containsKey("mutationRateStart")){
            String[] rates = ((String)options.get("mutationRateStart")).split(":");
            this.initialMutationRates = new double[numMutations];
            for (int i = 0; i < rates.length; ++i){
                initialMutationRates[i] = Double.parseDouble(rates[i]);
            }
        }
        if (options.containsKey("mutationRateEnd")){
            String[] rates = ((String)options.get("mutationRateEnd")).split(":");
            this.finalMutationRates = new double[numMutations];
            for (int i = 0; i < rates.length; ++i){
                finalMutationRates[i] = Double.parseDouble(rates[i]);
            }
        }
        // Set class strategies if they exist in the hash
        if (options.containsKey("populationSelection")){
            String tmp = ((String)options.get("populationSelection"));
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
        if (options.containsKey("reproductionSelection")){
            String tmp = ((String)options.get("reproductionSelection"));
            switch (tmp) {
                case "rankbased":
                    this.populationSelectionStrategy = new MuLambdaSelectionStrategy();
                    break;
                case "mulambda":
                    this.populationSelectionStrategy = new RankBasedSelectionStrategy();
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

    public GP(int generations, int populationSize, char analysisType){
        this.generations = generations;
        this.populationSize = populationSize;
        this.analysisType = analysisType;
        
        population = new ArrayList(populationSize);
    }
    
    public GP(int generations, int populationSize, double[] initialMutationRates, double[] finalMutationRates, double initialCrossoverProb, double finalCrossoverProb, char analysisType){
        /* Control Parameters */
        this.generations = generations;
        this.populationSize = populationSize;
        this.population = new ArrayList(populationSize);
        this.initialMutationRates = initialMutationRates;
        this.initialCrossoverProb = initialCrossoverProb;
        this.analysisType = analysisType;
        
        /* Strategies */
        initializationStrategy = new InitializationStrategy(analysisType);
        populationSelectionStrategy = new MuLambdaSelectionStrategy();
        crossoverStrategy = new SexualCrossoverStrategy(initialCrossoverProb, finalCrossoverProb);
        mutationStrategy = new TreeMutationStrategy(initialMutationRates, finalMutationRates);
    }
    
    public void run(){
        // Initialize population
        initializationStrategy.init(population, populationSize);
        
        // For each generation
        int t = 0;
        do {
            // Measure individuals
            for (Individual individual : population){
                individual.measure(t, new ArrayList());
            }
            
            // Clone previous generation P
            ArrayList<Individual> previousPopulation = new ArrayList();
            for (int i = 0; i < population.size(); ++i){
                previousPopulation.add(population.get(i).clone());
            }
            
            // Selection for reproduction
            //TODO: Agree on selection here with Stu
            ArrayList<Individual> candidatePopulation = reproductionSelectionStrategy.select(population, population.size()/2);
            
            // Reproduction producing P'
            double progress = (t / generations);
            ArrayList<Individual> crossoverOffspring = crossoverStrategy.crossover(candidatePopulation, progress);
            
            // Mutation producing P''
            ArrayList<Individual> mutationOffspring = mutationStrategy.mutate(crossoverOffspring, progress);
            
            // Select P(t+1) from union of offspring: P U P' U P''
            //previousPopulation.addAll(crossoverOffspring); //crossed over -- should we include these, even?
            previousPopulation.addAll(mutationOffspring);  //crossed over and mutated
            population = populationSelectionStrategy.select(previousPopulation, population.size());
            
            // Advance to next generation
            ++t;
            U.m(t);
            //printBest();
            //printPopulationFitnesses();
        } while (t < generations);
        
        printBest();
        
    }
    
    private void printBest(){
        Collections.sort(population,Individual.DescendingFitness);
        population.get(0).print();
    }
    
    private void printPopulationFitnesses(){
        Collections.sort(population, Individual.DescendingFitness);
        for (Individual i:population){
            U.p(i.getFitness() + ", ");
        }
        U.pl();
    }
    
}
