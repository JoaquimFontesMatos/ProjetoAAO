package org.example;

import java.util.Random;

public class FilterAndFanUFLP {

    private WarehouseLocationProblem problem;

    public FilterAndFanUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    public int[] initialSolution() {
        Random random = new Random(42); // Seed for reproducibility
        int[] solution = new int[problem.numCustomers];
        for (int j = 0; j < problem.numCustomers; j++) {
            solution[j] = random.nextInt(problem.numWarehouses);
        }
        return solution;
    }

    public double calculateCost(int[] solution) {
        double cost = 0.0;
        int[] warehouseUsage = new int[problem.numWarehouses];

        for (int j = 0; j < problem.numCustomers; j++) {
            int warehouse = solution[j];
            warehouseUsage[warehouse] += problem.demands[j];
            cost += problem.allocationCosts[warehouse][j];
        }

        for (int i = 0; i < problem.numWarehouses; i++) {
            if (warehouseUsage[i] > 0) {
                cost += problem.fixedCosts[i];
            }
        }

        return cost;
    }

    public int[] localSearch(int[] solution) {
        boolean improved = true;
        int iteration = 0;
        double currentCost = calculateCost(solution);

        while (improved && iteration < 100) { // Limit iterations to avoid infinite loops
            improved = false;

            for (int j = 0; j < problem.numCustomers; j++) {
                int currentWarehouse = solution[j];
                for (int i = 0; i < problem.numWarehouses; i++) {
                    if (i != currentWarehouse) {
                        solution[j] = i;
                        double newCost = calculateCost(solution);
                        if (newCost < currentCost) {
                            currentCost = newCost;
                            improved = true;
                        } else {
                            solution[j] = currentWarehouse;
                        }
                    }
                }
            }

            iteration++;
        }

        return solution;
    }

    public int[] filterAndFan() {
        int[] solution = initialSolution();
        solution = localSearch(solution);

        // Apply fan steps
        for (int step = 0; step < 3; step++) {
            solution = perturbSolution(solution);
            solution = localSearch(solution);
        }

        return solution;
    }

    private int[] perturbSolution(int[] solution) {
        Random random = new Random(42);

        for (int j = 0; j < problem.numCustomers; j++) {
            if (random.nextDouble() < 0.1) { // Probability of perturbation (adjust as needed)
                solution[j] = random.nextInt(problem.numWarehouses);
            }
        }

        return solution;
    }
}
