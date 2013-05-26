/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stuart
 */
public class GoogleExtractor extends Extractor {

    private final String fullFileName;
    private final String dateStarting, dateEnding, buyDate;
    private final String[] technicalIndicators;
    private ArrayList<String> outputLines;
    private ArrayList<Historical> historicalData;
    private ArrayList<Indicator> indicators;

//    /dates, TechnicalIndicators, fullFileName
    public GoogleExtractor(String[] dates, String[] tI, String fileName) {
        outputLines = new ArrayList();
        historicalData = new ArrayList();
        indicators = new ArrayList();

        technicalIndicators = tI;
        fullFileName = fileName;
        dateStarting = dates[0];
        dateEnding = dates[1];
        buyDate = dates[2];
    }

    public void extractBetweenDates() {
        try {
            outputLines = null;
            outputLines = new ArrayList();
            BufferedReader br = this.getBufferedReader(fullFileName);
            String outputFile = fullFileName.replaceAll(".csv", "") + " temp.csv";
            FileWriter fw = this.getFileWriter(outputFile);
            boolean inbetweendates = false;
            String line = br.readLine();

            while (line != null) {
                String[] lineItems;
                lineItems = line.split(",");
                if (lineItems[0].equals(dateStarting)) {
                    inbetweendates = true;
                }
                if (inbetweendates) {
                    outputLines.add(line);
                }
                if (lineItems[0].equals(dateEnding)) {
                    inbetweendates = false;
                }
                line = br.readLine();
            }
            for (int i = outputLines.size(); i > 0; i--) {
                fw.write(outputLines.get(i - 1));
                fw.write("\n");
                fw.flush();
            }
            getHistory(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getHistory(String fileName) {
        try {
            historicalData = new ArrayList();
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String rawLine = br.readLine();
            while (rawLine != null) {
                try {
                    String[] rawLineSplit = rawLine.split(",");
                    String date = rawLineSplit[0];
                    //O H L C V
                    double open = Double.parseDouble(rawLineSplit[1]);
                    double high = Double.parseDouble(rawLineSplit[2]);
                    double low = Double.parseDouble(rawLineSplit[3]);
                    double close = Double.parseDouble(rawLineSplit[4]);
                    int volume = Integer.parseInt(rawLineSplit[5]);
                    Historical historicalDataPoint = new Historical(date, open, high, low, close, volume);
                    historicalData.add(historicalDataPoint);
                } catch (Exception e) {
                }
                rawLine = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateTechnicalIndicators() {
        int size = historicalData.size();
        for (int i = 0; i < size; i++) {
            String date = historicalData.get(i).date;
            if (date.equals(buyDate)) {
                size = i;
                break;
            }
        }

        DecimalFormat df = new DecimalFormat("#0.000");

        for (int i = 0; i < size; i++) {
            String date = historicalData.get(i).date;
            String dateEnding = historicalData.get(size - 1).date;
            double close = historicalData.get(i).close;
            double closeEnding = historicalData.get(size - 1).close;
            double high = historicalData.get(i).high;
            double low = historicalData.get(i).low;
            int volume = historicalData.get(i).volume;

            //Accumulation Distribution Line calculation
            double accDist = ((close - low) - (high - close)) / (high - low) * volume;
            historicalData.get(i).setAccDist(accDist);
            if (date.equals("30-Dec-11")) {
                Indicator accumulativeDist = new Indicator("Accumulator Distribution", 1);
                accumulativeDist.values[0] = "" + df.format(historicalData.get(i).accDist);
                indicators.add(accumulativeDist);
            }
        }

        //Percentage closing price movements
        int[] percentChanges = {1, 7, 14, 30, 60, 90};
        for (int days : percentChanges) {
            double closeStart = historicalData.get(size - days).close;
            double closeEnd = historicalData.get(size).close;
            double percentChange = priceChange(closeEnd, closeStart);
            String name = days + " Day % price movement";
            Indicator percentChangeIndicator = new Indicator(name, 1);
            percentChangeIndicator.values[0] = "" + df.format(percentChange);
            indicators.add(percentChangeIndicator);
        }

        //Simple Moving Average
        int[] SMA = {7, 14, 30, 60, 90, 180};
        for (int sma : SMA) {
            double runningTotal = 0.0;
            for (int i = 0; i < sma; i++) {
                double close = historicalData.get(size - (1 + i)).close;
                runningTotal += close;
            }
            String name = sma + " Day Simple Moving Average";
            Indicator smavg = new Indicator(name, 1);
            smavg.values[0] = "" + df.format(runningTotal / sma);
            indicators.add(smavg);
        }

        //Rate of Change
        //ROC = [(Close - Close n periods ago) / (Close n periods ago)] * 100
        int[] periods = {1, 7, 14, 30, 60, 90};
        for (int period : periods) {
            double closeStart = historicalData.get(size - period).close;
            double closeEnd = historicalData.get(size - 1).close;
            double rateOfChange = ((closeEnd - closeStart) / closeEnd) * 100;
            String name = period + " Day % Rate of change";
            Indicator roc = new Indicator(name, 1);
            roc.values[0] = "" + df.format(rateOfChange);
            indicators.add(roc);
        }

        //Aroon calculations
        //Aroon-Up = ((25 - Days Since 25-day High)/25) x 100
        //Aroon-Down = ((25 - Days Since 25-day Low)/25) x 100
        double High25Day = Double.MIN_VALUE;
        double Low25Day = Double.MAX_VALUE;
        int daysSinceHigh = 0;
        int daysSinceLow = 0;
        for (int i = 25; i > 0; i--) {
            double close = historicalData.get(size - i).close;
            if (close > High25Day) {
                High25Day = close;
                daysSinceHigh = i;
            }
            if (close < Low25Day) {
                Low25Day = close;
                daysSinceLow = i;
            }
        }
        double AroonUp = ((25 - daysSinceHigh) / 25) * 100;
        Indicator aroonUp = new Indicator("Aroon up 25", 1);
        aroonUp.values[0] = "" + df.format(AroonUp);
        indicators.add(aroonUp);

        double AroonDown = ((25 - daysSinceLow) / 25) * 100;
        Indicator aroonDown = new Indicator("Aroon down 25", 1);
        aroonDown.values[0] = "" + df.format(AroonDown);
        indicators.add(aroonDown);

        //Force index
        //Force Index(1) = {Close (current period)  -  Close (prior period)} x Volume
        int[] forcePeriods = {1, 7, 14, 30, 60, 90};
        double[] forces = new double[6];
        int counter = 0;
        for (int force : forcePeriods) {
            double closeCurrent = historicalData.get(size - 1).close;
            double closePrior = historicalData.get(size - (1 + force)).close;
            double volume = historicalData.get(size - 1).volume;
            forces[counter] = (closeCurrent - closePrior) * volume;
            counter++;
        }
        double[] forceEMAs = new double[6];
        forceEMAs[5] = (forces[0] + (forces[1] / 2) + (forces[2] / 4) + (forces[3] / 8) + (forces[4] / 16) + (forces[5] / 32)) / 1000;
        forceEMAs[4] = (forces[0] + (forces[1] / 2) + (forces[2] / 4) + (forces[3] / 8) + (forces[4] / 16)) / 1000;
        forceEMAs[3] = (forces[0] + (forces[1] / 2) + (forces[2] / 4) + (forces[3] / 8)) / 1000;
        forceEMAs[2] = (forces[0] + (forces[1] / 2) + (forces[2] / 4)) / 1000;
        forceEMAs[1] = (forces[0] + (forces[1] / 2)) / 1000;
        forceEMAs[0] = (forces[0]) / 1000;
        for (int i = 0; i < 6; i++) {
            Indicator forceIndicator = new Indicator(forcePeriods[i] + " day force index", 1);
            forceIndicator.values[0] = "" + df.format(forceEMAs[i]);
            indicators.add(forceIndicator);
        }
    }

    private double priceChange(double close, double open) {
        double difference = close - open;
        double movement = (difference / open) * 100;
        //System.out.println("open: " + open + ", close: " + close + ", movement" + movement);
        return movement;
    }

    public void writeTechnicals() {
        try {
            String outputFile = fullFileName.replaceAll(".csv", "") + " Indicators.csv";
            FileWriter fw = new FileWriter(outputFile);
            for (Indicator indicator : indicators) {
                fw.write(indicator.indicatorName);
                for (int j = 0; j < indicator.values.length; j++) {
                    fw.write("," + indicator.values[j]);
                }
                fw.write("\n");
                fw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void writeTechnicals() {
     try {
     String outputFile = fullFileName.replaceAll(".csv", "") + " Indicators.csv";
     FileWriter fw = new FileWriter(outputFile);
     for (int i = 0; i < historicalData.size(); i++) {
     String date = historicalData.get(i).date;
     double open = historicalData.get(i).open;
     double close = historicalData.get(i).close;
     double high = historicalData.get(i).high;
     double low = historicalData.get(i).low;
     int volume = historicalData.get(i).volume;
     double accDist = historicalData.get(i).accDist;

     DecimalFormat df = new DecimalFormat("#.00000");

     String output = date + "," + open + "," + close + "," + high + "," + low + "," + volume + "," + df.format(accDist);
     System.out.println(output);
     fw.write(output);
     fw.write("\n");
     fw.flush();
     }
     } catch (Exception e) {
     e.printStackTrace();
     }
     }*/
}

class Historical {

    String date;
    double open, high, low, close;
    int volume;
    double accDist;

    Historical(String d, double o, double h, double l, double c, int v) {
        date = d;
        open = o;
        high = h;
        low = l;
        close = c;
        volume = v;
    }

    public void setAccDist(double accDist) {
        this.accDist = accDist;
    }
}


/*public void extractFitnessFile(String outputFileName) {
 try {
 FileWriter fw = new FileWriter("Fitness/" + outputFileName);

 String path = "Fitness";
 String files;
 File folder = new File(path);
 File[] listOfFiles = folder.listFiles();

 for (int i = 0; i < listOfFiles.length; i++) {

 if (listOfFiles[i].isFile()) {
 files = listOfFiles[i].getName();

 BufferedReader br = new BufferedReader(new FileReader("Fitness/" + files));
 String line = br.readLine();

 ArrayList<String> fitnessDates = new ArrayList();
 //fitnessDates.add("3-Jan-11");
 //fitnessDates.add("1-Apr-11");
 //fitnessDates.add("1-Jul-11");
 //fitnessDates.add("3-Oct-11");
 fitnessDates.add("30-Dec-11");

 while (line != null) {
 String[] lineSplit = line.split(",");
 if (fitnessDates.contains(lineSplit[0])) {
 fw.write(files.replaceAll(".csv", "") + "," + lineSplit[4] + "\n");
 fw.flush();
 }
 line = br.readLine();
 }
 }
 }
 } catch (IOException ex) {
 ex.printStackTrace();
 }
 }*/
