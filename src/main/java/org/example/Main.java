package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        WarehouseLocationProblem problem = new WarehouseLocationProblem("src/main/java/org/example/FicheirosTeste/ORLIB/ORLIB-uncap/a-c/capa.txt");

        // Executar o algoritmo Filter and Fan
        System.out.println("Executando Filter and Fan...");
        long startTime = System.nanoTime();

        FilterAndFanUFLP filterAndFanAlgorithm = new FilterAndFanUFLP(problem);
        int[] filterAndFanSolution = filterAndFanAlgorithm.filterAndFan();
        double filterAndFanCost = filterAndFanAlgorithm.calculateCost(filterAndFanSolution);

        long endTime = System.nanoTime();
        long durationFilterAndFan = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        System.out.println("Optimal solution cost (Filter and Fan): " + filterAndFanCost);
        System.out.println("Filter and Fan execution time: " + durationFilterAndFan + " ms");

        // Executar o algoritmo Tabu Search
        System.out.println("\nExecutando Tabu Search...");
        startTime = System.nanoTime();

        TabuSearchUFLP tabuSearchAlgorithm = new TabuSearchUFLP(problem);
        tabuSearchAlgorithm.solve();

        endTime = System.nanoTime();
        long durationTabuSearch = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        double tabuSearchCost = tabuSearchAlgorithm.getBestCost();

        System.out.println("Optimal solution cost (Tabu Search): " + tabuSearchCost);
        System.out.println("Tabu Search execution time: " + durationTabuSearch + " ms");

        // Executar o algoritmo Local Search
        System.out.println("\nExecutando Local Search...");
        startTime = System.nanoTime();

        LocalSearchUFLP localSearchAlgorithm = new LocalSearchUFLP(problem);
        localSearchAlgorithm.solve();

        endTime = System.nanoTime();
        long durationLocalSearch = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        double localSearchCost = localSearchAlgorithm.getBestCost();

        System.out.println("Optimal solution cost (Local Search): " + localSearchCost);
        System.out.println("Local Search execution time: " + durationLocalSearch + " ms");
    }

}