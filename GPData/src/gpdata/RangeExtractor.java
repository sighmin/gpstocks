/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpdata;

import java.io.BufferedReader;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author stuart
 */
public class RangeExtractor extends Extractor {

    public ArrayList<Range> ranges;

    RangeExtractor() {
        ranges = new ArrayList();
    }

    public void extractRanges() {
        try {
            String directory = "/home/stuart/Documents/GPStocks/GPFinance/data/Fundamentals";
            File[] files = this.getDirectoryListing(directory);
            initializeRanges(files[0]);
            for (File file : files) {
                BufferedReader br = this.getBufferedReader(file.getAbsolutePath());
                String line = br.readLine();
                int counter = 0;
                while (line != null) {
                    double MAX = ranges.get(counter).getMaximum();
                    double MIN = ranges.get(counter).getMinimum();
                    double value = Double.parseDouble(line.split(",")[1]);
                    if (value > MAX) {
                        ranges.get(counter).setMaximum(value);
                    }
                    if (value < MIN) {
                        ranges.get(counter).setMinimum(value);
                    }
                    ranges.get(counter).setMean((ranges.get(counter).getMean() + value));
                    line = br.readLine();
                    counter++;
                }
            }
            //For each indicator
            for (int i = 0; i < ranges.size(); i++) {
                double sumOfDiffs = 0.0;
                //In each file
                for (int j = 0; j < files.length; j++) {
                    BufferedReader br = this.getBufferedReader(files[j].getAbsolutePath());
                    String line = br.readLine();
                    int countLines = 0;
                    //Get the right line for the indicator
                    while (line != null) {
                        if (countLines == i) {
                            //Calculate the ranges sumOffDiffs
                            double mean = ranges.get(i).mean;
                            double value = Double.parseDouble(line.split(",")[1]);
                            sumOfDiffs += Math.pow((value - mean), 2.0);
                            DecimalFormat df = new DecimalFormat("#0.00");
                            System.out.print(df.format(sumOfDiffs) + ",");
                            //If you have processed all files update value for indicator
                            if (j == files.length - 1) {
                                //System.out.println("equal");
                                double valToSqrt = sumOfDiffs /  j;
                                double calculation = (double)Math.sqrt(valToSqrt);
                                ranges.get(i).setStdev(calculation);
                                System.out.print("\ncalc: " + calculation + " val: "+valToSqrt);
                            }
                        }
                        line = br.readLine();
                        countLines++;
                    }
                }
                System.out.println("");
            }
            for (Range range : ranges) {
                DecimalFormat df = new DecimalFormat("#0.00");
                range.setMean(range.getMean() / 61);
                System.out.println(range.indicatorName + ": "
                        + df.format(range.minimum) + "\t,"
                        + df.format(range.mean) + "\t,"
                        + df.format(range.maximum) + "\t,"
                        + df.format(range.stdev));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initializeRanges(File file) {
        try {
            BufferedReader br = this.getBufferedReader(file.getAbsolutePath());
            String line = br.readLine();
            while (line != null) {
                String name = line.split(",")[0];
                Range range = new Range(name, Double.MAX_VALUE, Double.MIN_VALUE, 0.0, 0.0);
                ranges.add(range);
                line = br.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        RangeExtractor r = new RangeExtractor();
        r.extractRanges();
    }
}

class Range {

    String indicatorName;
    double minimum, maximum;
    double mean;
    double stdev;

    Range(String name, double min, double max, double m, double s) {
        indicatorName = name;
        minimum = min;
        maximum = max;
        mean = m;
        stdev = s;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStdev() {
        return stdev;
    }

    public void setStdev(double stdev) {
        this.stdev = stdev;
    }
    
    
}
