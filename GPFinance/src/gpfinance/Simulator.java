package gpfinance;

import gpfinance.algorithm.*;
import gpfinance.datatypes.*;
import gpfinance.tree.*;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Simulator extends Thread {
    
    private String[] args = null;

    public Simulator(String[] args) { this.args = args; }

    @Override
    public void run() {
        // Parse
        // TODO parse this.args and create appropriate GP() and run it
        
        // Dispatch
        GP algorithm = new GP();
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
        }

        private void testDataTypes() {
            U.m("\n*** Testing data types");
            Decision[] decisions = {Decision.BUY, Decision.SELL};
            for (Decision d : decisions) {
                U.m(d);
            }

            Indicator[] techInd = {Tech.EXAMPLE};
            Indicator[] fundInd = {Fund.RAD};
            for (int i = 0; i < 5; ++i) {
                U.m(Fund.getRandom());
            }

            for (Indicator d : techInd) {
                U.m(d);
            }
            for (Indicator d : fundInd) {
                U.m(d);
            }
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
                nodes = tree.getRandomNonterminalNode();
                U.m(i + " - FINAL FOUND: prev=\"" + nodes[0] + "\", node=\"" + nodes[1] + "\"");
            }
            
            //trunc

            //
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
    }
}
