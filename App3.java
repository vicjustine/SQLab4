package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Multiclass Classification
 */
public class App {
    public static void main(String[] args) {
        String filePath = "src/main/resources/model.csv";
        System.out.println("\nEvaluating " + filePath);

        double crossEntropy = calculateCrossEntropy(filePath);
        System.out.println("\nCross Entropy = " + crossEntropy);
    }

    public static double calculateCrossEntropy(String filePath) {
        double sumCE = 0;
        int n = 0;
        try (FileReader filereader = new FileReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build()) {

            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                int actual = Integer.parseInt(row[0]) - 1;
                sumCE += Math.log(Double.parseDouble(row[actual + 1]) + 1e-10);
                n++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return Double.MAX_VALUE;
        }
        return -sumCE / n;
    }
}

