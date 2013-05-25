/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpdata;

import java.io.*;

/**
 *
 * @author stuart
 */
public class RenameFiles {

    public static void main(String args[]) {
        String path = "/home/stuart/Documents/GPStocks/GPData/Data/";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            try {
                if (!file.getName().contains("Technical")) {
                    BufferedReader br = new BufferedReader(new FileReader(path + file.getName()));
                    FileWriter fw = new FileWriter(path + file.getName().replaceAll(" Key Ratios.csv", "").toUpperCase() + " Fundamental");
                    String line = br.readLine();
                    while (line != null) {
                        fw.write(line);
                        fw.write("\n");
                        fw.flush();
                        line = br.readLine();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
