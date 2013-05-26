
package gpfinance.algorithm;

import gpfinance.algorithm.interfaces.SelectionStrategy;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class MuLambdaSelectionStrategy implements SelectionStrategy {

    public MuLambdaSelectionStrategy() { }
    
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> pool, int selectionSize){
        ArrayList<Individual> selected = new ArrayList(selectionSize);
        
        // sort pool by fitness
        Collections.sort(selected, Individual.MaximizeComparator);
        
        // loop from fittest individuals up to selectionSize
        for (int i = 0; i < selectionSize; ++i){
            selected.add(pool.get(i).clone());
        }
        
        return selected;
    }
    
    @Override
    public MuLambdaSelectionStrategy clone(){
        return new MuLambdaSelectionStrategy();
    }
}
