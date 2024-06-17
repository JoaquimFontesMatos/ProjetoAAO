package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WarehouseLocationProblem {
    int numWarehouses;
    int numCustomers;
    double[] capacities;
    float[] fixedCosts;
    int[] demands;
    float[][] allocationCosts;

    public WarehouseLocationProblem(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            numWarehouses = scanner.nextInt();
            numCustomers = scanner.nextInt();

            this.capacities = new double[numWarehouses];
            this.fixedCosts = new float[numCustomers];
            this.demands = new int[numCustomers];
            this.allocationCosts = new float[numWarehouses][numCustomers];

            System.out.println("numFacilities: " + numWarehouses);
            System.out.println("numCustomers: " + numCustomers);

            capacities = new double[numWarehouses];
            fixedCosts = new float[numWarehouses];
            demands = new int[numCustomers];
            allocationCosts = new float[numWarehouses][numCustomers];

            for (int i = 0; i < numWarehouses; i++) {
                capacities[i] = scanner.nextDouble();
                //System.out.println("capacity" + i + ": " + capacities[i]);

                String input = scanner.nextLine();
                fixedCosts[i] = Float.parseFloat(input.trim());
                //System.out.println("fixedCost" + i + ": " + fixedCosts[i]);
            }

            for (int i = 0; i < numCustomers; i++) {
                demands[i] = scanner.nextInt();
                //System.out.println("demand" + i + ": " + demands[i]);

                for (int j = 0; j < numWarehouses; j++) {
                    String input = scanner.next();
                    allocationCosts[j][i]=Float.parseFloat(input.trim());
                    //System.out.println("cost" + j + "." + i + ": " + allocationCosts[j][i]);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao carregar o arquivo de dados: " + e.getMessage());
        }
    }
}