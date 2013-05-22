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
public class GoogleExtractor {

    ArrayList<String> outputLines;
    ArrayList<Historical> historicalData;

    GoogleExtractor() {
        outputLines = new ArrayList();
        historicalData = new ArrayList();
    }

    public void extractDates(String dateStarting, String dateEnding, String fileName) {
        outputLines = null;
        outputLines = new ArrayList();

        try {
            FileReader fr = new FileReader("Fitness/" + fileName);
            BufferedReader br = new BufferedReader(fr);

            //String outputFile = fileName.replaceAll(".csv", "") + "-2010-2011.csv";
            String outputFile = "Fitness/" + fileName.replaceAll(".csv", "").toUpperCase() + ".csv";
            FileWriter fw = new FileWriter(outputFile);

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

            fw.close();
            br.close();
            fr.close();
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

    public void calculateAccDist() {
        for (int i = 0; i < historicalData.size(); i++) {
            double close = historicalData.get(i).close;
            double high = historicalData.get(i).high;
            double low = historicalData.get(i).low;
            int volume = historicalData.get(i).volume;

            double accDist = ((close - low) - (high - close)) / (high - low) * volume;
            historicalData.get(i).setAccDist(accDist);
        }
    }

    public void writeTechnicals(String fileName) {
        try {
            String outputFile = fileName.replaceAll(".csv", "") + "-technicals.csv";
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

            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void extractFitnessFile(String outputFileName) {
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
    }

    public static void main(String args[]) {
        GoogleExtractor extractor = new GoogleExtractor();

        String path = "Fitness";
        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        /*for (int i = 0; i < listOfFiles.length; i++) {

         if (listOfFiles[i].isFile()) {
         //files = listOfFiles[i].getName();
         //extractor.extractDates("30-Dec-11", "4-Jan-10", files);
         /*extractor.getHistory("Google/" + files);
         extractor.calculateAccDist();
         extractor.writeTechnicals("Google/" + files);*/
        //  }
        //}
        extractor.extractFitnessFile("Fitness-30-Dec-11.csv");
    }
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
