
package gpfinance.algorithm;

import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class TreeMutationStrategy implements MutationStrategy {
    
    double[] rates;
    
    public TreeMutationStrategy(double[] rates){
        this.rates = rates;
    }
    
    @Override
    public ArrayList<Individual> mutate(ArrayList<Individual> population, double progress){
        ArrayList<Individual> mutatedOffspring = new ArrayList();
        return mutatedOffspring;
    }
}
