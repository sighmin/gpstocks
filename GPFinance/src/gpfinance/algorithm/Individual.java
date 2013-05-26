
package gpfinance.algorithm;

import gpfinance.U;
import gpfinance.datatypes.Decision;
import gpfinance.datatypes.Fitness;
import gpfinance.datatypes.Security;
import gpfinance.tree.DecisionTree;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Individual {
    private DecisionTree tree;
    private Fitness fitness;
    
    public Individual() {}
    
    public Individual(char type){
        this.tree = new DecisionTree(type);
    }

    public Individual(char type, int treesize){
        this.tree = new DecisionTree(type, treesize);
    }

    public Individual(DecisionTree tree) {
        this.tree = tree;
    }
    
    public Individual(DecisionTree tree, Fitness fitness) {
        this.tree = tree;
    }
    
    @Override
    public Individual clone(){
        return new Individual(this.tree.clone(), this.fitness.clone());
    }
    
    public void measure(int generation, ArrayList<Security> securities){
        // Decision[] evaluate() and create list of decisions
        Decision[] decisions = tree.evaluate(securities);
        
        // Fitness calculateReturn(Decision[])
        
    }
    
    public double getFitness(){
        return this.fitness.getFitness();
    }
    
    public static Comparator<Individual> MinimizeComparator = new Comparator<Individual>(){
        @Override
        public int compare(Individual o1, Individual o2) {
            Double d1 = o1.fitness.getFitness();
            Double d2 = o2.fitness.getFitness();
            return d1.compareTo(d2);
        }
    };
    
    public static Comparator<Individual> MaximizeComparator = new Comparator<Individual>(){
        @Override
        public int compare(Individual o1, Individual o2) {
            Double d1 = o1.fitness.getFitness();
            Double d2 = o2.fitness.getFitness();
            return d2.compareTo(d1);
        }
    };
    
    
    public DecisionTree getTree() {
        return tree;
    }

    public void print(){
        U.m("f(): " + fitness);
        tree.print();
    }

    /*
     * Mutation methods, delegate to tree. 
     */
    public void mutateGrow() {
        tree.insertRandom();
    }

    public void mutateTrunc() {
        tree.removeRandomLimitedDepth();
    }

    public void mutateGauss() {
        tree.gaussRandom();
    }

    public void mutateSwapInequality() {
        tree.swapRandomInequality();
    }

    public void mutateLeaf() {
        tree.mutateTerminalNode();
    }

    public void mutateNonLeaf() {
        tree.mutateNonterminalNode();
    }
}
