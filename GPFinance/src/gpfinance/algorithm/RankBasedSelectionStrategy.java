
package gpfinance.algorithm;

import gpfinance.U;
import gpfinance.algorithm.interfaces.SelectionStrategy;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class RankBasedSelectionStrategy implements SelectionStrategy {
    
    public RankBasedSelectionStrategy(){ }
    
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> pool){
        return null;
    }
    
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> pool, int selectionSize){
        ArrayList<Individual> selected = new ArrayList(selectionSize);
        
        // Rank pool (sort)
        Collections.sort(pool, Individual.IndividualComparator);
        
        // Calculate probabilities
        int poolsize = pool.size();
        double[] probs = new double[poolsize];
        double probsum = 0.0;
        for (int i = 0; i < poolsize; ++i){ probsum += i+1; }
        for (int i = 0; i < poolsize; ++i){
            probs[i] = ((double) i+1) / probsum;
        }
        
        // Sample ranked pool
        probsum = 0.0;
        for (int i = 0; i < selectionSize; ++i){
            probsum = 0.0;
            double r = U.r();
            for (int j = 0; j < poolsize; ++j){
                probsum += probs[j];
                if (probsum > r){
                    selected.add(pool.get(j));
                    break; // get another random value for next candidate selection
                }
            }
        }
        
        return selected;
    }
    
    @Override
    public RankBasedSelectionStrategy clone(){
        return new RankBasedSelectionStrategy();
    }
}
