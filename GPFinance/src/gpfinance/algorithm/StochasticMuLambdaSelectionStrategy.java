
package gpfinance.algorithm;

import gpfinance.U;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class StochasticMuLambdaSelectionStrategy extends MuLambdaSelectionStrategy {
    
    private double restartRates[] = {0.4, 0.02};
    
    public StochasticMuLambdaSelectionStrategy() { }
    
    public StochasticMuLambdaSelectionStrategy(double[] restartRates){
        this.restartRates = restartRates;
    }
    
    @Override
    public ArrayList<Individual> selectDynamic(ArrayList<Individual> pool, int selectionSize, double progress) {
        ArrayList<Individual> selected = new ArrayList();
        
        // sort pool by fitness
        Collections.sort(pool, Individual.DescendingFitness);
        
        // loop from fittest individuals up to selectionSize
        for (int i = 0; i < selectionSize; ++i){
            selected.add(pool.get(i).clone());
        }
        pool.removeAll(selected);
        
        // perform a random restart dynamically according to the progress
        double rate = (restartRates[1] * progress) + (restartRates[0] * (1.0 - progress));
        int numRestarts = (int)(rate * selected.size());
        
        ArrayList<Individual> restarts = new ArrayList();

        // Rank pool (sort)
        Collections.sort(pool, Individual.DescendingFitness);

        // Calculate probabilities
        int poolsize = selected.size();
        double[] probs = new double[poolsize];
        double probsum = 0.0;
        for (int i = 0; i < poolsize; ++i) {
            probsum += i + 1;
        }
        for (int i = 0; i < poolsize; ++i) {
            probs[i] = ((double) i + 1) / probsum;
        }

        // Sample ranked pool
        for (int i = 0; i < numRestarts; ++i) {
            probsum = 0.0;
            double r = U.r();
            for (int j = 0; j < selected.size(); ++j) {
                probsum += probs[j];
                if (probsum > r) {
                    restarts.add(selected.get(j).clone());
                    break; // get another random value for next candidate selection
                }
            }
        }
        selected.removeAll(restarts);
        
        // Add randomly selected individuals from the pool back in to selected
        for (int i = 0; i < numRestarts; ++i){
            selected.add(pool.get(U.rint(pool.size())));
        }
        
        return selected;
    }
    
    @Override
    public StochasticMuLambdaSelectionStrategy clone(){
        double newRates[] = new double[2];
        for (int i = 0; i < 2; ++i)
            newRates[i] = this.restartRates[i];
        return new StochasticMuLambdaSelectionStrategy(newRates);
    }
}
