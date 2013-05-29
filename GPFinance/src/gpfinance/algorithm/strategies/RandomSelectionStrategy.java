
package gpfinance.algorithm.strategies;

import gpfinance.algorithm.Individual;
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
    public ArrayList<Individual> select(ArrayList<Individual> pool, int selectionSize){
        ArrayList<Individual> selected = new ArrayList();
        
        Random random = new Random();
        for (int i = 0; i < selectionSize; ++i){
            int r = random.nextInt(pool.size());
            selected.add(pool.get(r).clone());
        }
        
        return selected;
    }
    
    @Override
    public RandomSelectionStrategy clone(){
        return new RandomSelectionStrategy();
    }

    @Override
    public ArrayList<Individual> selectDynamic(ArrayList<Individual> pool, int selectionSize, double progress) {
        return select(pool, selectionSize);
    }
}
