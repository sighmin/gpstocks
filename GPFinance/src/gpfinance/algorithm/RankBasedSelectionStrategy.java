
package gpfinance.algorithm;

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
        
        // Sample ranked pool
        
        
        return selected;
    }
    
    @Override
    public RankBasedSelectionStrategy clone(){
        return new RankBasedSelectionStrategy();
    }
}
