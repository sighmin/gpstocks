
package gpfinance.algorithm;

import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public interface CrossoverStrategy {
    public ArrayList<Individual> crossover(ArrayList<Individual> population, double progress);
}
