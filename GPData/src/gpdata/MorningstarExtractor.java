/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpdata;

import java.io.*;
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

    public void writeDataToFile() {
        try {
            String outputFileName = morningstarFileName.replaceAll(".csv", "") + " Indicators.csv";
            BufferedReader br;
            FileWriter fw = this.getFileWriter(outputFileName);
            for (int i = 0; i < rawData.size(); i++) {
                if (indicatorsToExtract.contains(rawData.get(i).indicatorName)) {
                    fw.write(rawData.get(i).indicatorName);
                    for (int j = 0; j < rawData.get(i).values.length; j++) {
                        fw.write("," + rawData.get(i).values[j]);
                    }

                    fw.write("\n");
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
