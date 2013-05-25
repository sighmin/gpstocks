/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpdata;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MorningstarExtractor extends Extractor {

    String morningstarFileName;
    ArrayList<Indicator> rawData;
    ArrayList<Integer> yearsToExtract;
    ArrayList<String> indicatorsToExtract;

    MorningstarExtractor(String[] years, String[] indicators, String fileName) {
        super();
        initializeVariables(years, indicators);
        morningstarFileName = fileName;
    }

    private void initializeVariables(String[] years, String[] indicators) {
        rawData = new ArrayList();
        indicatorsToExtract = new ArrayList();
        indicatorsToExtract.addAll(Arrays.asList(indicators));
        initYearsToExtract(years);
    }

    private void initYearsToExtract(String[] years) {
        yearsToExtract = new ArrayList();

        for (String year : years) {
            try {
                int yearInt = Integer.parseInt(year);
                switch (yearInt) {
                    case 2012:
                        yearsToExtract.add(11);
                        break;
                    case 2011:
                        yearsToExtract.add(10);
                        break;
                    case 2010:
                        yearsToExtract.add(9);
                        break;
                    case 2009:
                        yearsToExtract.add(8);
                        break;
                    case 2008:
                        yearsToExtract.add(7);
                        break;
                    case 2007:
                        yearsToExtract.add(6);
                        break;
                    case 2006:
                        yearsToExtract.add(5);
                        break;
                    case 2005:
                        yearsToExtract.add(4);
                        break;
                    case 2004:
                        yearsToExtract.add(3);
                        break;
                    case 2003:
                        yearsToExtract.add(2);
                        break;
                }
            } catch (Exception exception) {
                System.out.println("Error caught: " + exception);
            }
        }
    }

    public void extractData() throws Exception {
        try (BufferedReader br = this.getBufferedReader(morningstarFileName)) {
            String line = br.readLine();
            while (line != null) {
                String[] lineSplitComma = line.split(",");
                if (lineSplitComma.length > 10) {
                    int size = yearsToExtract.size();
                    Indicator indicator = new Indicator(lineSplitComma[0], size);
                    for (int i = 0; i < yearsToExtract.size(); i++) {
                        indicator.values[i] = lineSplitComma[yearsToExtract.get(i)];
                    }
                    rawData.add(indicator);
                }
                line = br.readLine();
            }
        }
    }

    private double change(double close, double open) {
        double difference = close - open;
        double movement = (difference / close) * close;
        return movement;
    }

    public void writeDataToFile() {
        try {
            DecimalFormat df = new DecimalFormat("#0.000");
            String outputFileName = morningstarFileName.replaceAll(".csv", "") + " Indicators.csv";
            BufferedReader br;
            FileWriter fw = this.getFileWriter(outputFileName);
            for (int i = 0; i < rawData.size(); i++) {
                if (indicatorsToExtract.contains(rawData.get(i).indicatorName)) {

                    String indicatorValue2009 = rawData.get(i).values[0];
                    String indicatorValue2010 = rawData.get(i).values[1];
                    if (indicatorValue2009.equals("")) {
                        indicatorValue2009 = indicatorValue2010;
                    }

                    fw.write(rawData.get(i).indicatorName + " 2009," + indicatorValue2009 + "\n");
                    fw.write(rawData.get(i).indicatorName + " 2010," + indicatorValue2010 + "\n");

                    double indicatorValue2009D = Double.parseDouble(indicatorValue2009);
                    double indicatorValue2010D = Double.parseDouble(indicatorValue2010);
                    double change = change(indicatorValue2009D, indicatorValue2010D);
                    fw.write(rawData.get(i).indicatorName + " YoY % change," + df.format(change) + "\n");

                    fw.flush();
                }
            }
            br = this.getBufferedReader(outputFileName);
            String lineOut = br.readLine();
            boolean hadRD = false;
            while (lineOut != null) {
                String lineOutSplit[] = lineOut.split(",");
                if (lineOutSplit[0].equals("R&D")) {
                    hadRD = true;
                    break;
                }
                lineOut = br.readLine();
            }
            if (hadRD == false) {
                String RNDAvgOut = "R&D,13.575769,13.575769";
                fw.write(RNDAvgOut);
                System.out.println("R&D");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
