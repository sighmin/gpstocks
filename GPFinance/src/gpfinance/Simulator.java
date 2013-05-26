package gpfinance;

import gpfinance.algorithm.*;
import gpfinance.algorithm.interfaces.SelectionStrategy;
import gpfinance.datatypes.*;
import gpfinance.tree.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Simulator extends Thread {
    
    private String[] args = null;
    public ArrayList<Security> securities;

    public Simulator(String[] args) { this.args = args; }

    @Override
    public void run() {
        // Parse
        String[] pair;
        HashMap<String, String> options = new HashMap();
        for (int i = 0; i < args.length; ++i){
            pair = args[i].split("=");
            if (pair.length == 2)
                options.put(pair[0], pair[1]);
        }
        
        // Dispatch
        GP algorithm = new GP(options);
        algorithm.securities = this.securities;
        algorithm.run();
    }
    
    public void test() {
        Test test = new Test(args);
    }

    private class Test {
        public Test(String[] args) {
            // Parse args
            String c = "";
            for (String s : args) {
                c += s + " ";
            }

            // Dispatch
            if (c.contains("all")) {
                testDataTypes();
                testTree();
                testRandomGenerators();
                initialization();
                selection();
                mutation();
                crossover();
            }
            if (c.contains("datatypes")) {
                testDataTypes();
            }
            if (c.contains("tree")) {
                testTree();
            }
            if (c.contains("random")) {
                testRandomGenerators();
            }
            if (c.contains("all-operators")){
                initialization();
                selection();
                mutation();
                crossover();
            }
            if (c.contains("initialization")){
                initialization();
            }
            if (c.contains("selection")){
                selection();
            }
            if (c.contains("mutation")){
                mutation();
                mutationRates();
            }
            if (c.contains("crossover")){
                crossover();
            }
            if (c.contains("clone")){
                clonetest();
            }
            if (c.contains("adhoc")){
                adhoc();
            }
        }

        private void testDataTypes() {
            U.m("\n*** Testing data types");
            Decision[] decisions = {Decision.BUY, Decision.SELL};
            for (Decision d : decisions) {
                U.m(d);
            }

            Indicator[] techInd = {Tech.EXAMPLE};
            Indicator[] fundInd = {Fund.GrossMargin09};
            for (int i = 0; i < 5; ++i) {
                U.m(Fund.getRandom());
            }

            for (Indicator d : techInd) {
                U.m(d);
            }
            for (Indicator d : fundInd) {
                U.m(d);
            }
            
            // data type cloning (deep copies)
        }

        private void testTree() {
            // init tree
            U.m("\n*** Testing trees");
            DecisionTree tree = new DecisionTree('F', 5);
            // size
            U.m("Size: " + tree.size());
            U.m("Avg depth: " + tree.avgDepth());
            // print
            U.m("Print tree");
            tree.print();

            U.m("Finding random positions in tree...");
            Node[] nodes;
            for (int i = 0; i < 3; ++i){
                nodes = tree.getRandomNonterminalNode(false);
                U.m(i + " - FINAL FOUND: prev=\"" + nodes[0] + "\", node=\"" + nodes[1] + "\"");
            }
            
            // trunc
            
        }

        private void testRandomGenerators() {
            U.m("\n*** Testing random");
            U.m("...test randomVal()");
            for (int i = 0; i < 10; ++i) {
                U.m(U.randomVal());
            }
            U.m("...test gauss random");
            for (int i = 0; i < 10; ++i) {
                U.m(U.getRandomGauss((double)i));
            }
        }

        private void initialization() {
            char type = 'F';
            int numindividuals = 5;
            Individual[] ins = new Individual[numindividuals];
            for (int i = 0; i < numindividuals; ++i){
                ins[i] = new Individual(type);
            }
            for (int i = 0; i < numindividuals; ++i){
                U.m("Tree #" + i);
                ins[i].print();
            }
        }

        private void selection() {
            
            U.m("**************** Testing random selection strategy...");
            SelectionStrategy rand = new RandomSelectionStrategy();
            int numpop = 10;
            int selectionSize = 5;
            ArrayList<Individual> pop = new ArrayList();
            for (int i = 0; i < numpop; ++i){
                pop.add(new Individual('F', 0));
                pop.get(i).measure(U.rint(numpop), new ArrayList());
            }
            
            U.m("Testing  sort...");
            U.m("BEFORE");
            for (int i = 0; i < numpop; ++i){
                pop.get(i).print();
            }
            Collections.sort(pop, Individual.DescendingFitness);
            U.m("AFTER");
            for (int i = 0; i < numpop; ++i){
                pop.get(i).print();
            }
            
            U.m("RANDOM - POOL:");
            for (int i = 0; i < numpop; ++i){
                pop.get(i).print();
            }
            ArrayList<Individual> selected = rand.select(pop, selectionSize);
            U.m("RANDOM - SELECTED:");
            for (int i = 0; i < selected.size(); ++i){
                selected.get(i).print();
            }
            
            U.m("**************** Testing MuLambda selection strategy...");
            rand = new MuLambdaSelectionStrategy();
            
            U.m("MULAMBDA - POOL:");
            for (int i = 0; i < numpop; ++i){
                pop.get(i).print();
            }
            selected = rand.select(pop, selectionSize);
            U.m("MULAMBDA - SELECTED:");
            for (int i = 0; i < selected.size(); ++i){
                selected.get(i).print();
            }
            
            U.m("**************** Testing Rank based selection strategy...");
            rand = new RankBasedSelectionStrategy();
            
            U.m("RANK - POOL:");
            for (int i = 0; i < numpop; ++i){
                pop.get(i).print();
            }
            selected = rand.select(pop, selectionSize);
            U.m("RANK - SELECTED:");
            for (int i = 0; i < selected.size(); ++i){
                selected.get(i).print();
            }
        }

        private void crossover() {
            // init individuals
            int num = 1;
            Individual[] i1 = new Individual[num];
            Individual[] i2 = new Individual[num];
            for (int i = 0; i < num; ++i){
                i1[i] = new Individual('F');
                i2[i] = new Individual('F');
            }
            // print before
            U.m("Before crossover:");
            for (int i = 0; i < num; ++i){
                i1[i].print();
                i2[i].print();
            }
            
            // crossover
            SexualCrossoverStrategy cross = new SexualCrossoverStrategy(0.8, 0.4);
            /*for (int k = 0; k < 10; ++k){
                for (int i = 0; i < num; ++i){
                    cross.crossoverPair(i1[i], i2[i]);
                }
            }*/
            
            // print after
            U.m("After crossover:");
            for (int i = 0; i < num; ++i){
                i1[i].print();
                i2[i].print();
            }
            
            
            
        }
        
        private void mutation() {
            
            Individual in = new Individual('F');
            int mutations = 10;
            
            //grow
            U.m("Before grow:");
            in.print();
            for (int i = 0; i < mutations; ++i){
                in.mutateGrow();
            }
            U.m("After grow:");
            in.print();
            
            
            //trunc
           /*
            * as seen below (performing half of the trunc operations as mutations)
            * since trunc chooses a random non terminal and replaces it was a terminal
            * node, effectively chopping off an entire subtree... I think we should
            * maybe take depth into account here, otherwise with large trees it may
            * become too destructive, simply chopping potentially good branches.
            */
            
            U.m("Before trunc:");
            in.print();
            for (int i = 0; i < mutations/2; ++i){
                in.mutateTrunc();
            }
            U.m("After trunc:");
            in.print();
            
            
            //swap inequality
            U.m("Before swap inequality:");
            in.print();
            for (int i = 0; i < mutations; ++i){
                in.mutateSwapInequality();
            }
            U.m("After swap inequality:");
            in.print();
            
            //non-terminal (indicator)
            U.m("Before indicator mutate:");
            in.print();
            for (int i = 0; i < mutations; ++i){
                in.mutateNonLeaf();
            }
            U.m("After indicator mutate:");
            in.print();
            
            //terminal (swap decisionnode)
            U.m("Before leaf:");
            in.print();
            for (int i = 0; i < mutations; ++i){
                in.mutateLeaf();
            }
            U.m("After leaf:");
            in.print();
            
            //gauss
            U.m("Before gauss:");
            in.print();
            for (int i = 0; i < mutations; ++i){
                in.mutateGauss();
            }
            U.m("After gauss:");
            in.print();
            
            
            
        }
        
        private void mutationRates(){
            int t = 0;
            int generations = 500;
            ArrayList<Individual> pop = new ArrayList(50);
            for (int i = 0; i < 50; ++i){
                pop.add(new Individual('F', 5));
            }
            
            //                              {grow,  trunc, indicator, leaf, inequality, gauss}
            double[] initialMutationRates = {1.0, 0.0,   0.8,       0.8,  0.8,        0.9};
            //                              {grow,  trunc, indicator, leaf, inequality, gauss}
            double[] finalMutationRates =   {0.5, 0.5,   0.2,       0.2,  0.2,        0.4};
            
            TreeMutationStrategy mu = new TreeMutationStrategy(initialMutationRates, finalMutationRates);
            
            
            for (int i = 0; i < 6; ++i){
                U.p(initialMutationRates[i] + "\t");
            }
            U.pl("");
            
            for (int i = 0; i < generations; ++i){
                mu.mutate(pop, (double) i/generations);
            }
            
            for (int i = 0; i < 6; ++i){
                U.p(finalMutationRates[i] + "\t");
            }
            U.pl("");
        }
        
        private void clonetest(){
            // clone testing
            DecisionTree tree = new DecisionTree('F');
            DecisionTree betterTree = tree.clone();
            
            U.m("Before edit original\n*** tree:");
            tree.print();
            U.m("*** betterTreee");
            betterTree.print();
            
            // Alter betterTree and see if tree changes at all...
            for (int i = 0; i < 5; ++i){
                tree.insertRandom();
                tree.gaussRandom();
                tree.gaussRandom();
                tree.mutateNonterminalNode();
                tree.mutateTerminalNode();
                tree.swapRandomInequality();
                tree.removeRandomLimitedDepth();
            }
            
            U.m("After edit original\n*** tree:");
            tree.print();
            U.m("*** betterTreee");
            betterTree.print();
        }
        
        private void adhoc(){
        }
    }
}
