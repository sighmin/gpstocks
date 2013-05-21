
package gpfinance;


/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class GPFinance {
    public static void main(String[] args) {
        // Parse args
        String cmds = "";
        for (String s : args)
            cmds += s + " ";
        
        // Dispatch command
        if (cmds.contains("test")){
            test(args);
        } else if (cmds.contains("run")){
            run(args);
        } else if (cmds.contains("help")){
            help();
        } else {
            usage();
        }
    }
    
    public static void run(String[] args){
        // Parse args
        
        // Run Simulation
        
    }
    
    public static void test(String[] args){
        new Simulator().test(args);
    }
    
    public static void usage(){
        U.pl(""
                + "NAME\n"
                + "    GPFiance.jar - Uses genetic programming to evolve a decision "
                + "tree to buy/short securities in a financial market by using "
                + "fundamental or technical indicators.\n\n"
                + "USAGE\n"
                + "    java -jar GPFinance.jar help\n"
                + "    java -jar GPFinance.jar run [fundamental|technical] [OPTIONS]\n"
                + "    java -jar GPFinance.jar test [all|random|tree|datatypes]\n");
    }
    
    public static void help(){
        U.pl(""
                + "NAME\n"
                + "    GPFiance.jar - Uses genetic programming to evolve a decision "
                + "tree to buy/short securities in a financial market by using "
                + "fundamental or technical indicators."
                + "\n\n"
                + "SYNOPSIS\n"
                + "    java -jar GPFinance.jar run [OPTIONS]\n"
                + "    java -jar GPFinance.jar test [TEST_OPTIONS]"
                + "\n\n"
                + "DESCRIPTION\n"
                + "    Evolve decision trees to buy/short securities by using either "
                + "fundamental or technical indicators, given a quartly evaluation period."
                + "\n\n"
                + "  OPTIONS\n"
                + "      fundamental\n"
                + "          Develops decision tree using fundametal indicators.\n"
                + "      technical\n"
                + "          Develops decision tree using technical indicators.\n"
                + "  TEST_OPTIONS\n"
                + "      all\n"
                + "          Runs all the tests. This option is particularly verbose.\n"
                + "      random\n"
                + "          Tests the random generators and resulting random data types.\n"
                + "      datatypes\n"
                + "          Tests the abstract data types and enumerated types.\n"
                + "      tree\n"
                + "          Tests the decision tree implementation operations.\n"
                + "");
    }
    
}
