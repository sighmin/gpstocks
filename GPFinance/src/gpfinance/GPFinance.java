package gpfinance;

import gpfinance.datatypes.Security;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class GPFinance {

    public static void main(String[] args) {
        // Parse args
        String cmds = "";
        for (String s : args) {
            cmds += s + " ";
        }

        // Dispatch command
        try {
            if (cmds.contains("test")) {
                test(args);
            } else if (cmds.contains("run")) {
                run(args);
            } else if (cmds.contains("help")) {
                help();
            } else {
                usage();
            }
        } catch (Exception e) {
            U.pl("Exception caught in main.");
            U.pl(e.getMessage());
            e.printStackTrace();
        } finally {
            U.m("\nGPFinance run complete.");
        }
    }

    public static void run(String[] args) throws Exception {
        final int numsims = 1;
        Simulator[] sims = new Simulator[numsims];

        // Read in the data for evaluating the tree
        ArrayList<Security> securitiesData = new ArrayList();
        File f = new File("###.txt");
        String path = f.getAbsolutePath().replaceAll("/###.txt", "") + "/data/Fundamentals/";
        path = path.replaceAll("/dist", "");
        f.delete();
        File folder = new File(path);
        System.out.println("folder " + folder);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().contains("Fundamental Indicators")) {
                try {
                    //Read in the file
                    FileReader fr = new FileReader(path + file.getName());
                    BufferedReader br = new BufferedReader(fr);
                    String tickerSymbol = file.getName().replaceAll(" Fundamental Indicators.csv", "");
                    double[] indicatorData = new double[42];
                    String line = br.readLine();
                    int counter = 0;
                    while (line != null) {
                        String[] lineSplit = line.split(",");
                        indicatorData[counter] = Double.parseDouble(lineSplit[1]);
                        line = br.readLine();
                        counter++;
                    }
                    Security security = new Security(tickerSymbol, indicatorData);
                    securitiesData.add(security);
                } catch (Exception err) {
                    System.out.println("Error reading in security data " + err);
                    err.printStackTrace();
                }
            }
        }

        // Run
        for (int i = 0; i < numsims; ++i) {
            sims[i] = new Simulator(args);
            sims[i].securities = securitiesData;
            sims[i].run();
        }
        for (Simulator s : sims) {
            s.join();
        }
    }

    public static void test(String[] args) throws Exception {
        (new Simulator(args)).test();
    }

    public static void usage() {
        U.pl(""
                + "NAME\n"
                + "    GPFiance.jar - Uses genetic programming to evolve a decision "
                + "tree to buy/short securities in a financial market by using "
                + "fundamental or technical indicators.\n\n"
                + "USAGE\n"
                + "    java -jar GPFinance.jar help\n"
                + "    java -jar GPFinance.jar run [OPTIONS]\n"
                + "    java -jar GPFinance.jar test [all|random|tree|datatypes]\n");
    }

    public static void help() {
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
                + "      type=[fundamental|technical]\n"
                + "          Develops decision tree using fundametal indicators.\n"
                + "          Develops decision tree using technical indicators.\n"
                + "      generations=<number>\n"
                + "          The number of iterations before the genetic algorithm halts.\n"
                + "      population=<number>\n"
                + "          The number of individuals in the population.\n"
                + "      crossoverRate=<number>:<number>\n"
                + "          The initial and final crossover rate, changes linearly.\n"
                + "      mutationRateStart=<number>:<number>:<number>:<number>:<number>:<number>\n"
                + "          Set of initial mutation rates for types grow, trunc, indicator, decision, inequality and gauss.\n"
                + "      mutationRateEnd=<number>:<number>:<number>:<number>:<number>:<number>\n"
                + "          Set of final mutation rates of the same type as above.\n"
                + "      populationSelection=[mulambda|rankbased|random]\n"
                + "          Sets the next population selection strategy.\n"
                + "      reproductionSelection=[rankbased|mulambda|random]\n"
                + "          Sets the reproduction selection strategy.\n"
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
