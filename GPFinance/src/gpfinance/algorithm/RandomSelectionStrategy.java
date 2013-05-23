
package gpfinance.algorithm;

import gpfinance.algorithm.interfaces.SelectionStrategy;
import java.util.ArrayList;
import java.util.Random;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class RandomSelectionStrategy implements SelectionStrategy {
    
    public RandomSelectionStrategy(){ }
    
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> pool){
        return null;
    }
    
    @Override
    public ArrayList<Individual> select(ArrayList<Individual> pool, int selectionSize){
        ArrayList<Individual> selected = new ArrayList(selectionSize);
        ArrayList<Individual> poolCopy = new ArrayList(pool.subList(0, pool.size()-1));
        
        Random random = new Random();
        for (int i = 0; i < selectionSize; ++i){
            int r = random.nextInt(poolCopy.size());
            selected.add(poolCopy.remove(r));
        }
        
        return selected;
    }
    
    @Override
    public RandomSelectionStrategy clone(){
        return new RandomSelectionStrategy();
    }
}
