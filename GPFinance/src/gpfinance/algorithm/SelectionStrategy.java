
package gpfinance.algorithm;

import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public interface SelectionStrategy {  
    /**
     * Method takes a pool of individuals and applies a selection
     * strategy and returns the selected individuals.
     * 
     * @param pool of individuals competing for selection
     * @return ArrayList of selected individuals
     */
    public ArrayList<Individual> select(ArrayList<Individual> pool, int selection_total);
}
