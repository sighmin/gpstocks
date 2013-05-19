
package gpfinance.algorithm;

import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public interface SelectionStrategy {
    public ArrayList<Individual> select(ArrayList<Individual> pool);
    public ArrayList<Individual> select(ArrayList<Individual> pool, int selectionSize);
}
