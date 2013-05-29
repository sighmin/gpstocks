
package gpfinance.algorithm.strategies;

import gpfinance.U;
import gpfinance.algorithm.Individual;
import gpfinance.algorithm.interfaces.CrossoverStrategy;
import gpfinance.tree.Node;
import java.util.ArrayList;
import java.util.Random;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class SexualCrossoverStrategy implements CrossoverStrategy {
    
    private double initialProb;
    private double finalProb;
    private final int CANDIDATE = 0;
    private final int RAND_CANDIDATE = 1;

    public SexualCrossoverStrategy(double initialProb, double finalProb) {
        this.initialProb = initialProb;
        this.finalProb = finalProb;
    }
    
    @Override
    public SexualCrossoverStrategy clone(){
        return new SexualCrossoverStrategy(this.initialProb, this.finalProb);
    }
    
    @Override
    public ArrayList<Individual> crossover(ArrayList<Individual> selectedPop, double progress){
        ArrayList<Individual> crossoverOffspring = new ArrayList();
        
        // Calc dynamic probability of crossover
        int size = selectedPop.size();
        Random random = new Random();
        double prob = (progress * finalProb) + ((1.0 - progress) * initialProb);

        // Create list of pairs of parents
        ArrayList<Individual[]> pairs = new ArrayList();
        for (int i = 0; i < size; ++i){
            double r = U.r();
            if (r < prob){
                Individual[] pair = new Individual[2];
                pair[CANDIDATE] = selectedPop.get(i).clone();
                pair[RAND_CANDIDATE] = selectedPop.get(U.rint(size)).clone(); //select another random individual for the above candidate
                pairs.add(pair);
            }
        }
        
        // Perform crossover on all pairs
        for (int i = 0; i < pairs.size(); ++i){
            // destructively crossover pair
            Individual[] pair = pairs.get(i);
            crossoverPair(pair[CANDIDATE], pair[RAND_CANDIDATE]);
            // add crossedover parents to crossedover list
            crossoverOffspring.add(pair[CANDIDATE]);
            crossoverOffspring.add(pair[RAND_CANDIDATE]);
        }
        
        return crossoverOffspring;
    }
    
    //TODO: swap this back to private access
    protected void crossoverPair(Individual parent1, Individual parent2){
        final int PREV = 0; final int CURR = 1;
        // Destructive crossover, meaning, it changes the parents into the offspring
        Node[] p1nodes = parent1.getTree().getRandomNonterminalNode(false);
        Node[] p2nodes = parent2.getTree().getRandomNonterminalNode(false);
        
        // if getting a node failed (fringe case, but necessary condition here)
        if (p1nodes == null || p2nodes == null){
            return;
        }
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
