package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single Variable Binary Regression
 */
public class App {
    public static void main(String[] args) {
        String filePath = "src/main/resources/model.csv";
        System.out.println("\nEvaluating " + filePath);

        double[] metrics = calculateMetrics(filePath);
        System.out.println("BCE = " + metrics[0]);
        System.out.println("Accuracy = " + metrics[1]);
        System.out.println("Precision = " + metrics[2]);
        System.out.println("Recall = " + metrics[3]);
        System.out.println("F1-score = " + metrics[4]);
    }

    public static double[] calculateMetrics(String filePath) {
        List<Double> trueValues = new ArrayList<>();
        List<Double> predictedValues = new ArrayList<>();

        try (FileReader filereader = new FileReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build()) {

            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                trueValues.add(Double.parseDouble(row[0]));  
                predictedValues.add(Double.parseDouble(row[1]));  
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new double[]{Double.MAX_VALUE, 0, 0, 0, 0};
        }

        double bce = calculateBCE(trueValues, predictedValues);
        return new double[]{bce, 0, 0, 0, 0};
    }

    public static double calculateBCE(List<Double> trueValues, List<Double> predictedValues) {
        double sum = 0;
        int n = trueValues.size();
        for (int i = 0; i < n; i++) {
            double y = trueValues.get(i);
            double yHat = predictedValues.get(i);
            sum += y * Math.log(yHat + 1e-10) + (1 - y) * Math.log(1 - yHat + 1e-10);
        }
        return -sum / n;
    }
}

