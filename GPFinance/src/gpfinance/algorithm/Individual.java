
package gpfinance.algorithm;

import gpfinance.tree.DecisionTree;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Individual {
    private DecisionTree tree;
    private double fitness = Double.NEGATIVE_INFINITY;
    
    public Individual(char type){
        this.tree = new DecisionTree(type);
    }

    public Individual(DecisionTree tree) {
        this.tree = tree;
    }
    
    @Override
    public Individual clone(){
        return null;
    }
    
    public void measure(int t){
        
    }
}
