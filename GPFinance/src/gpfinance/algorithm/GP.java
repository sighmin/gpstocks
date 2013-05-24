
package gpfinance.algorithm;

import gpfinance.algorithm.interfaces.SelectionStrategy;
import gpfinance.algorithm.interfaces.MutationStrategy;
import gpfinance.algorithm.interfaces.CrossoverStrategy;
import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class GP {
    /* Control Parameters */
    private int generations = 5000;
    private int populationSize = 50;
    private ArrayList<Individual> population = new ArrayList(populationSize);
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] initialMutationRates = {1.0, 0.0,   0.75,       0.75,  0.75,        0.9};
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] finalMutationRates =   {0.4, 0.5,   0.2,       0.4,  0.2,        0.4};
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

    public GP(int generations, int populationSize, char analysisType){
        this.generations = generations;
        this.populationSize = populationSize;
        this.analysisType = analysisType;
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
        initializationStrategy.init(population);
        
        // For each generation
        int t = 0;
        do {
            // Measure individuals
            for (Individual individual : population){
                individual.measure(t);
            }
            
            // Clone previous generation P
            ArrayList<Individual> previousPopulation = new ArrayList(population.size());
            for (int i = 0; i < previousPopulation.size(); ++i){
                previousPopulation.set(i, population.get(i).clone());
            }
            
            // Selection for reproduction
            ArrayList<Individual> candidatePopulation = reproductionSelectionStrategy.select(population, population.size()/2);
            
            // Reproduction producing P'
            double progress = (t / generations);
            ArrayList<Individual> crossoverOffspring = crossoverStrategy.crossover(candidatePopulation, progress);
            
            // Mutation producing P''
            ArrayList<Individual> mutationOffspring = mutationStrategy.mutate(crossoverOffspring, progress);
            
            // Select P(t+1) from union of offspring: P U P' U P''
            previousPopulation.addAll(crossoverOffspring); //crossed over -- should we include these, even?
            previousPopulation.addAll(mutationOffspring);  //crossed over and mutated
            population = populationSelectionStrategy.select(previousPopulation, population.size());
            
            // Advance to next generation
            ++t;
        } while (t < generations);
    }
    
}
