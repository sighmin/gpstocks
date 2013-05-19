
package gpfinance.algorithm;

import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class SexualCrossoverStrategy implements CrossoverStrategy {
    
    private double probability;
    private SelectionStrategy selectionStrategy;

    public SexualCrossoverStrategy(double probability, SelectionStrategy selectionStrategy) {
        this.probability = probability;
        this.selectionStrategy = selectionStrategy;
    }
    
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> population, double progress){
        return null;
    }
}
