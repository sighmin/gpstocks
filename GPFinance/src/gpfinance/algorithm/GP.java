
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
    private ArrayList<Individual> population = new ArrayList<Individual>();
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    private double[] mutationRate = {100.0, 0.0,   0.4,       0.4,  0.4,        0.4};
    private double crossoverProb = 0.6;
    
    /* Strategies */
    private InitializationStrategy initializationStrategy;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;

    // generate constructors once all instance variables defined
    public GP(){ }
    
    public void run(){
        // Initialize population
        initializationStrategy.init(population);
        
        // For each generation
        int t = 0;
        do {
            // Measure individuals
            for (Individual individual : population){
                individual.measure();
            }
            
            // Reproduction producing P'
            ArrayList<Individual> offspring = crossoverStrategy.crossover(population, (t / generations) * 100.0, selectionStrategy);
            
            // Mutate P (allows broader mutation of )
            mutationStrategy.mutate(population, (t / generations) * 100.0, selectionStrategy);
            
            // Select P(t+1) from P U P'
            offspring.addAll(population);
            population = selectionStrategy.select(offspring, population.size());
            
            // Advance to next generation
            ++t;
        } while (t < generations);
    }
    
}
