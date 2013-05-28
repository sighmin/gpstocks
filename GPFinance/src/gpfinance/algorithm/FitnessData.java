/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpfinance.algorithm;

import gpfinance.datatypes.Decision;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author stuart
 */
public class FitnessData {

    public String[] fitnessFiles = new String[2];
    public int quarter, returnColumn;
    ArrayList<Return> returns;

    FitnessData(String[] fileNames, int q) {
        fitnessFiles[0] = fileNames[0];
        fitnessFiles[1] = fileNames[1];
        quarter = q;

        int[] returnColums = {3, 5, 7, 9};
        returnColumn = returnColums[quarter - 1];

        returns = new ArrayList();
        populateReturns();
    }

    private void populateReturns() {
        // Open the Fitness.csv given 2 possible filepaths
        FileReader fr = null;
        for (int i = 0; i < 2; ++i) {
            try {
                fr = new FileReader(fitnessFiles[i]);
            } catch (FileNotFoundException e) {
                // silently try the second filepath (it will be either in Stuarts path or Simons path)
            }
        }
        
        // Read in data from the file
        try {
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                String[] lineSplit = line.split(",");
                String name = lineSplit[0];
                double returnVal = Double.parseDouble(lineSplit[returnColumn]);
                Return stockReturn = new Return(name, returnVal);
                returns.add(stockReturn);
                line = br.readLine();
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double calculateReturn(Decision[] decisions) {
        //System.out.println("L: " + decisions.length);

        double calculatedReturn = 0.0;
        int counter = 0;
        for (Return ret : returns) {
            if (decisions[counter] == Decision.BUY) {
                calculatedReturn += ret.quarterlyReturn;
            } else {
                calculatedReturn += 0 - ret.quarterlyReturn;
            }
            counter++;
        }
        return calculatedReturn / decisions.length;
    }
}

class Return {

    String ticker;
    double quarterlyReturn;

    Return(String name, double ret) {
        ticker = name;
        quarterlyReturn = ret;
    }
}
