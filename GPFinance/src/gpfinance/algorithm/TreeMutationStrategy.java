
package gpfinance.algorithm;

import gpfinance.algorithm.interfaces.MutationStrategy;
import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class TreeMutationStrategy implements MutationStrategy {
    
    
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
  //private double[] mutationRates = {100.0, 0.0,   0.4,       0.4,  0.4,        0.4};
    double[] rates;
    
    public TreeMutationStrategy(double[] rates){
        this.rates = rates;
    }
    
    @Override
    public TreeMutationStrategy clone(){
        return new TreeMutationStrategy(this.rates.clone());
    }
    
    @Override
    public ArrayList<Individual> mutate(ArrayList<Individual> population, double progress){
        ArrayList<Individual> mutatedOffspring = new ArrayList();
        return mutatedOffspring;
    }
    
    private void growMutation(Individual individual){
        individual.mutateGrow();
    }
    
    private void truncMutation(Individual individual){
        individual.mutateTrunc();
    }
    
    private void gaussianMutation(Individual individual){
        // mutate a non-terminal nodes value
        individual.mutateGauss();
    }
    
    private void swapMutation(Individual individual){
        // change inequality of a non-terminal node
        individual.mutateSwapInequality();
    }
    
    private void terminalNodeMutation(Individual individual){
        // swap out a terminal node with it's opposite
        individual.mutateLeaf();
    }
    
    private void nonterminalNodeMutation(Individual individual){
        // swap out a non-terminal nodes indicator
        individual.mutateNonLeaf();
    }
}
