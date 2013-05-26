/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpdata;

import java.io.File;

/**
 *
 * @author stuart
 */
public class GPData {

    private MorningstarExtractor fundamentalExtractor;
    private GoogleExtractor technicalExtractor;
    private String[] FundamentalIndicators;
    private String[] TechnicalIndicators;
    private String[] years;
    private String[] dates;

    public GPData(String[] fI, String[] tI, String[] yrs, String[] dts, String filePath) {
        try {
            FundamentalIndicators = fI;
            TechnicalIndicators = tI;
            years = yrs;
            dates = dts;
            extractAllData(filePath);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void extractAllData(String filePath) {
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fullFileName = filePath + file.getName();
                if (file.getName().contains("Fundamental")) {
                    try {
                        fundamentalExtractor = new MorningstarExtractor(years, FundamentalIndicators, fullFileName);
                        fundamentalExtractor.extractData();
                        fundamentalExtractor.writeDataToFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (file.getName().contains("Technical")) {
                    try {
                        technicalExtractor = new GoogleExtractor(dates, TechnicalIndicators, fullFileName);
                        technicalExtractor.extractBetweenDates();
                        technicalExtractor.calculateTechnicalIndicators();
                        technicalExtractor.writeTechnicals();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().contains("temp")) {
                file.delete();
            }
        }
    }

    public static void main(String[] args) {
        String[] yearsToExtract = {"2009", "2010"};
        String[] datesToExtract = {"30-Dec-11", "4-Jan-10", "3-Jan-11"};
        String[] fundamentals = {"Gross Margin %", "Operating Margin %",
            "Earnings Per Share USD", "Book Value Per Share USD", "SG&A",
            "R&D", "Return on Assets %", "Return on Equity %",
            "Return on Invested Capital %", "Cap Ex as a % of Sales",
            "Total Current Assets", "Total Current Liabilities",
            "Current Ratio", "Quick Ratio"};
        String[] technicals = {};
        GPData extractor = new GPData(fundamentals, technicals, yearsToExtract, datesToExtract, "/home/stuart/Documents/GPStocks/GPData/Data/");
    }
}
