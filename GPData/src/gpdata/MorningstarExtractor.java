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
import java.util.ArrayList;

public class MorningstarExtractor {

    String morningstarFileName;
    ArrayList<Indicator> rawData;
    ArrayList<Integer> yearsToExtract;
    ArrayList<String> indicatorsToExtract;

    MorningstarExtractor(String[] years, String[] indicatorsToE, String fileName) {
        rawData = new ArrayList();
        yearsToExtract = new ArrayList();
        indicatorsToExtract = new ArrayList();

        morningstarFileName = fileName;

        for (int i = 0; i < indicatorsToE.length; i++) {
            indicatorsToExtract.add(indicatorsToE[i]);
        }
        
        if (years[0].equals("2003")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2004")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2005")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2006")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2007")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2008")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2009")) {
            yearsToExtract.add(9);
        }
        if (years[0].equals("2010")) {
            yearsToExtract.add(9);
        }
        if (years[1].equals("2011")) {
            yearsToExtract.add(10);
        }
    }

    public void extractData() {
        try {
            FileReader fr = new FileReader(morningstarFileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                String[] lineSplitComma = line.split(",");
                if (lineSplitComma.length > 5) {
                    int size = yearsToExtract.size();
                    Indicator indicator = new Indicator(lineSplitComma[0], size);
                    for (int i = 0; i < yearsToExtract.size(); i++) {
                        indicator.values[i] = lineSplitComma[yearsToExtract.get(i)];
                    }
                    rawData.add(indicator);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
        }
    }

    public void printData() {
        for (int i = 0; i < rawData.size(); i++) {
            if (indicatorsToExtract.contains(i)) {
                System.out.print(rawData.get(i).indicatorName);

                for (int j = 0; j < rawData.get(i).values.length; j++) {
                    System.out.print("," + rawData.get(i).values[j]);
                }
                System.out.print("\n");
            }
        }
    }

    public void writeDataToFile() {
        try {
            String outputFileName = morningstarFileName.replaceAll(" Key Ratios.csv", "") + "_summary.csv";
            FileWriter fw = new FileWriter(outputFileName);
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
        } catch (Exception e) {
        }
    }

    public static void main(String args[]) {
        String[] years = {"2010", "2011"};
        /*
 2 gross margin
 4 operating margin
 6 EPS 
 10 Book value per share
 20 SG&A as a % of sales
 21 R&D as a % of sales
 29 Return on assets
 31 Return on equity
 32 Return on invested capital
 53 Cap Ex as a % of Sales (growth yoy)
 59 Inventory (growth yoy)
 61 Total Current Assets (growth yoy)
 70 Total Current Liabilities (growth yoy)
 76 Current Ratio
 77 Quick Ratio
 85 Inventory Turnover
 */
        String[] indicators = {
            "Gross Margin %",
            "Operating Margin %",
            "Earnings Per Share USD",
            "Book Value Per Share USD",
            "SG&A",
            "R&D",
            "Return on Assets %",
            "Return on Equity %",
            "Return on Invested Capital %",
            "Cap Ex as a % of Sales",
            "Inventory",
            "Total Current Assets",
            "Total Current Liabilities",
            "Current Ratio",
            "Quick Ratio",
            "Inventory Turnover"};

        // Directory path here
        String path = "Morningstar";
        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                
                MorningstarExtractor extractor = new MorningstarExtractor(years, indicators, "Morningstar/"+files);
                extractor.extractData();
                extractor.writeDataToFile();
            }
        }
    }
}

class Indicator {

    String indicatorName;
    String[] values;

    Indicator(String name, int valuesSize) {
        indicatorName = name;
        values = new String[valuesSize];
    }
}
