package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        WarehouseLocationProblem problem = new WarehouseLocationProblem("src/main/java/org/example/FicheirosTeste/ORLIB/ORLIB-uncap/130/cap131.txt");

        // Executar o algoritmo Filter and Fan
        System.out.println("Executando Filter and Fan...");
        long startTime = System.nanoTime();

        FilterAndFanUFLP filterAndFanAlgorithm = new FilterAndFanUFLP(problem);
        int[] filterAndFanSolution = filterAndFanAlgorithm.filterAndFan();
        double filterAndFanCost = filterAndFanAlgorithm.calculateCost(filterAndFanSolution);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        System.out.println("Optimal solution cost (Filter and Fan): " + filterAndFanCost);
        //for (int i = 0; i < filterAndFanSolution.length; i++) {
            //System.out.println("Customer " + i + " assigned to warehouse " + filterAndFanSolution[i]);
        //}
        System.out.println("Filter and Fan execution time: " + duration + " ms");

        // Executar o algoritmo Tabu Search
        System.out.println("\nExecutando Tabu Search...");
        startTime = System.nanoTime();

        TabuSearchUFLP tabuSearchAlgorithm = new TabuSearchUFLP(problem);
        tabuSearchAlgorithm.solve();

        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Tabu Search execution time: " + duration + " ms");

        // Executar o algoritmo Local Search 5 vezes
        System.out.println("\nExecutando Local Search...");
        long totalDuration = 0;
        double totalCost = 0;

        for (int run = 0; run < 50; run++) {
            startTime = System.nanoTime();

            LocalSearchUFLP localSearchAlgorithm = new LocalSearchUFLP(problem);
            localSearchAlgorithm.solve();

            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

            totalDuration += duration;
            totalCost += localSearchAlgorithm.getBestCost();

            //System.out.println("Local Search run " + (run + 1) + " execution time: " + duration + " ms");
        }

        double averageDuration = totalDuration / 50.0;
        double averageCost = totalCost / 50.0;

        System.out.println("Average Local Search execution time: " + averageDuration + " ms");
        System.out.println("Average Local Search solution cost: " + averageCost);
    }
}