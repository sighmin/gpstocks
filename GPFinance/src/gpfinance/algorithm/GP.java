
package gpfinance.algorithm;

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
    private double[] mutationRate = {100.0, 0.0,   0.4,       0.4,  0.4,        0.4};
    private double crossoverProb = 0.8;
    
    /* Strategies */
    private InitializationStrategy initializationStrategy = new InitializationStrategy();
    private SelectionStrategy selectionStrategy = new MuLambdaSelectionStrategy();
    private CrossoverStrategy crossoverStrategy = new SexualCrossoverStrategy(crossoverProb, new RankBasedSelectionStrategy());
    private MutationStrategy mutationStrategy = new TreeMutationStrategy(mutationRate);

    // generate constructors once all instance variables defined
    public GP(){ /* Create GP with default parameters */ }
    
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
            
            // Clone previous generation, P
            ArrayList<Individual> previousPopulation = new ArrayList<Individual>(population.size());
            for (int i = 0; i < previousPopulation.size(); ++i){
                previousPopulation.set(i, population.get(i).clone());
            }
            
            // Reproduction producing P'
            double progress = (t / generations) * 100.0;
            ArrayList<Individual> crossoverOffspring = crossoverStrategy.crossover(population, progress);
            
            // Mutate producing P''
            ArrayList<Individual> mutationOffspring = mutationStrategy.mutate(population, progress);
            
            // Select P(t+1) from P U P' U P''
            previousPopulation.addAll(crossoverOffspring);
            previousPopulation.addAll(mutationOffspring);
            population = selectionStrategy.select(previousPopulation, population.size());
            
            // Advance to next generation
            ++t;
        } while (t < generations);
    }
    
}
