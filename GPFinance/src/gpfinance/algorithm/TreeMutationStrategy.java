package gpfinance.algorithm;

import gpfinance.U;
import gpfinance.algorithm.interfaces.MutationStrategy;
import java.util.ArrayList;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class TreeMutationStrategy implements MutationStrategy {

    private final int numMutations = 6;
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    //private double[] initialRates = {1.0, 0.0,   0.6,       0.6,  0.6,        0.6};
    private double[] initialRates;
    //                              {grow,  trunc, indicator, leaf, inequality, gauss}
    //private double[] finalRates   = {0.5, 0.5,   0.2,       0.2,  0.2,        0.2};
    private double[] finalRates;

    public TreeMutationStrategy(double[] initialRates, double[] finalRates) {
        this.initialRates = initialRates;
        this.finalRates = finalRates;
    }

    @Override
    public TreeMutationStrategy clone() {
        double[] newinitialRates = new double[numMutations];
        double[] newfinalRates = new double[numMutations];
        for (int i = 0; i < numMutations; ++i) {
            newinitialRates[i] = initialRates[i];
            newfinalRates[i] = finalRates[i];
        }
        return new TreeMutationStrategy(newinitialRates, newfinalRates);
    }

    @Override
    public ArrayList<Individual> mutate(ArrayList<Individual> selectedPop, double progress) {
        ArrayList<Individual> mutatedOffspring = new ArrayList();
        int popsize = selectedPop.size();
        double[] rates = new double[numMutations];

        // Update mutation rates given progress
        //U.p("progress: (" + progress + ") ");
        for (int i = 0; i < numMutations; ++i) {
            double scaledRate = (progress * finalRates[i]) + ((1.0 - progress) * initialRates[i]);
            rates[i] = scaledRate;
            //U.p(scaledRate + "\t\t");
        }
        //U.p("\n");

        // Mutate
        // for every individual
        for (int i = 0; i < popsize; ++i) {
            // for every type of mutation
            Individual individual = selectedPop.get(i).clone();
            for (int j = 0; j < numMutations; ++j) {
                double r = U.r();
                // grow or trunc rates
                if (j == 0 || j == 1) {
                    if (r < rates[j]) {
                        switch (j) {
                            case 0:
                                individual.mutateGrow();
                                break;
                            case 1:
                                individual.mutateTrunc();
                                break;
                        }
                    }
                    // other mutation rates
                } else {
                    if (r < (rates[j] / 2.0)) { // necessary so mutation isn't too large a change on the tree
                        switch (j) {
                            case 2:
                                individual.mutateNonLeaf();
                                break;
                            case 3:
                                individual.mutateLeaf();
                                break;
                            case 4:
                                individual.mutateSwapInequality();
                                break;
                            case 5:
                                individual.mutateGauss();
                                break;
                        }
                    }
                }
            } // end mutation types
            mutatedOffspring.add(individual);
        }

        return mutatedOffspring;
    }
}
