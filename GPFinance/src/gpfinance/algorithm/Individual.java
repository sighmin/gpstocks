
package gpfinance.algorithm;

import gpfinance.tree.DecisionTree;
import java.util.Comparator;

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
    
    public static Comparator<Individual> IndividualComparator = new Comparator<Individual>(){
        @Override
        public int compare(Individual o1, Individual o2) {
            Double d1 = o1.fitness;
            Double d2 = o2.fitness;
            return d1.compareTo(d2);
        }
    };
}
