
package gpfinance.algorithm.strategies;

import gpfinance.algorithm.Individual;
import gpfinance.tree.Node;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class SexualRootCrossoverStrategy extends SexualCrossoverStrategy {

    public SexualRootCrossoverStrategy(double initialProb, double finalProb) {
        super(initialProb, finalProb);
    }
    
    @Override
    protected void crossoverPair(Individual parent1, Individual parent2){
        // Destructive crossover, meaning, it changes the parents into the offspring
        Node p1root = parent1.getTree().getRoot();
        Node p2root = parent2.getTree().getRoot();
        
        // Swap left sub branches of root.
        Node tempLeftRoot = p1root.left;
        p1root.left = p2root.left;
        p2root.left = tempLeftRoot;
    }
}
