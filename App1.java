package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single Variable Continuous Regression
 */
public class App {
    public static void main(String[] args) {
        String[] models = {"model_1.csv", "model_2.csv", "model_3.csv"};
        String bestModelMSE = "", bestModelMAE = "", bestModelMARE = "";
        double minMSE = Double.MAX_VALUE, minMAE = Double.MAX_VALUE, minMARE = Double.MAX_VALUE;

        for (String model : models) {
            System.out.println("\nEvaluating " + model);
            double[] metrics = calculateMetrics("src/main/resources/" + model);

            System.out.println("MSE = " + metrics[0]);
            System.out.println("MAE = " + metrics[1]);
            System.out.println("MARE = " + metrics[2]);

            if (metrics[0] < minMSE) {
                minMSE = metrics[0];
                bestModelMSE = model;
            }
            if (metrics[1] < minMAE) {
                minMAE = metrics[1];
                bestModelMAE = model;
            }
            if (metrics[2] < minMARE) {
                minMARE = metrics[2];
                bestModelMARE = model;
            }
        }

        System.out.println("\nBest Model Based on Metrics:");
        System.out.println("According to MSE, The best model is " + bestModelMSE);
        System.out.println("According to MAE, The best model is " + bestModelMAE);
        System.out.println("According to MARE, The best model is " + bestModelMARE);
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
            return new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        }

        double mse = calculateMSE(trueValues, predictedValues);
        double mae = calculateMAE(trueValues, predictedValues);
        double mare = calculateMARE(trueValues, predictedValues);

        return new double[]{mse, mae, mare};
    }

    public static double calculateMSE(List<Double> trueValues, List<Double> predictedValues) {
        double sum = 0;
        int n = trueValues.size();
        for (int i = 0; i < n; i++) {
            sum += Math.pow(trueValues.get(i) - predictedValues.get(i), 2);
        }
        return sum / n;
    }

    public static double calculateMAE(List<Double> trueValues, List<Double> predictedValues) {
        double sum = 0;
        int n = trueValues.size();
        for (int i = 0; i < n; i++) {
            sum += Math.abs(trueValues.get(i) - predictedValues.get(i));
        }
        return sum / n;
    }

    public static double calculateMARE(List<Double> trueValues, List<Double> predictedValues) {
        double sum = 0;
        int n = trueValues.size();
        double epsilon = 1e-10;
        for (int i = 0; i < n; i++) {
            sum += Math.abs(trueValues.get(i) - predictedValues.get(i)) / (Math.abs(trueValues.get(i)) + epsilon);
        }
        return (sum / n) * 100;  
    }
}

