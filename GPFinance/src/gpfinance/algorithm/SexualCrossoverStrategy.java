
package gpfinance.algorithm;

import gpfinance.algorithm.interfaces.SelectionStrategy;
import gpfinance.algorithm.interfaces.CrossoverStrategy;
import gpfinance.tree.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class SexualCrossoverStrategy implements CrossoverStrategy {
    
    private double probability;
    private SelectionStrategy selectionStrategy;

    public SexualCrossoverStrategy(double probability, SelectionStrategy selectionStrategy) {
        this.probability = probability;
        this.selectionStrategy = selectionStrategy;
    }
    
    @Override
    public SexualCrossoverStrategy clone(){
        return new SexualCrossoverStrategy(this.probability, this.selectionStrategy.clone());
    }
    
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> population, double progress){
        ArrayList<Individual> crossoverOffspring = new ArrayList();
        
        // Calulate rank-based probabilities
        Collections.sort(population, Individual.IndividualComparator);
        int size = population.size();
        double sum = 0;
        Random random = new Random();
        double[] probabilities = new double[size];
        for (int i = 0; i < size; ++i){
            sum += (double) (i+1);
        }
        for (int i = 0; i < size; ++i){
            probabilities[i] = (double) (i+1) / (double) size;
        }
        
        // Create list of pairs of parents
        ArrayList<Individual[]> pairs = new ArrayList();
        // if rand() > prob[i], select it!
        for (int i = 0; i < size; ++i){
            double rand = Math.random();
            if (rand > probabilities[i]){
                Individual[] tempPair = new Individual[2];
                tempPair[0] = population.get(i).clone();
                pairs.add(tempPair);
            }
        }
        
        // for every selected individual from above selection, select another
        int numSelected = 0;
        int k = 0;
        do {
            double rand = Math.random();
            if (rand > probabilities[k % size]){
                ++numSelected;
                Individual[] tempPair = pairs.get(k % pairs.size());
                tempPair[1] = population.get(k % size).clone();
            }
            ++k;
        } while (numSelected < pairs.size());
        
        // Select pairs that will crossover & perfrom crossover using probability
        for (int i = 0; i < pairs.size(); ++i){
            double rand = Math.random();
            if (rand > probability){
                // destructively crossover pair
                Individual offspring1;
                Individual offspring2;
                crossoverPair(offspring1 = pairs.get(i)[0], offspring2 = pairs.get(i)[1]);
                
                // add crossedover parents to crossedover list
                crossoverOffspring.add(offspring1);
                crossoverOffspring.add(offspring2);
            }
        }
        
        return crossoverOffspring;
    }
    
    private void crossoverPair(Individual parent1, Individual parent2){
        final int PREV = 0; final int CURR = 1;
        // Destructive crossover, meaning, it changes the parents into the offspring
        Node[] p1nodes = parent1.getTree().getRandomNonterminalNode(false);
        Node[] p2nodes = parent2.getTree().getRandomNonterminalNode(false);
        Node temp = p1nodes[CURR];
        
        // Replace subtree of parent1
        if (p1nodes[PREV].left == p1nodes[CURR]){
            p1nodes[PREV].left = p2nodes[CURR];
        } else {
            p1nodes[PREV].right = p2nodes[CURR];
        }
        
        // Replace subtree of parent2
        if (p2nodes[PREV].left == p2nodes[CURR]){
            p2nodes[PREV].left = temp;
        } else {
            p2nodes[PREV].right = temp;
        }
    }
}
