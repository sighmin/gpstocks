package gpfinance;

import gpfinance.datatypes.*;
import gpfinance.tree.*;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Simulator {

    public Simulator() {
    }

    public void run() {
    }
    
    public void test(String[] args) {
        new Test(args);
    }

    private class Test {

        public Test(String[] args) {
            // Parse
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
            U.pl("*** Testing data types");
            Decision[] decisions = {Decision.BUY, Decision.SELL};
            for (Decision d : decisions) {
                U.pl(d);
            }

            Indicator[] techInd = {Tech.EXAMPLE};
            Indicator[] fundInd = {Fund.RAD};
            for (int i = 0; i < 5; ++i) {
                U.pl(Fund.getRandom());
            }

            for (Indicator d : techInd) {
                U.pl(d);
            }
            for (Indicator d : fundInd) {
                U.pl(d);
            }
        }

        private void testTree() {
            // init tree
            U.pl("*** Testing trees");
            DecisionTree tree = new DecisionTree('F', 5);
            // size
            U.pl("Size: " + tree.size());
            // print
            U.pl("Small tree display method:");
            tree.displaySmallTree();
            
            U.pl("Large tree display method:");
            tree = new DecisionTree('F', 25);
            tree.displayLargeTree();


            //grow

            //trunc

            //
        }

        private void testRandomGenerators() {
            U.pl("*** Testing random");
            for (int i = 0; i < 10; ++i) {
                U.pl(U.randomVal());
            }
        }
    }
}
