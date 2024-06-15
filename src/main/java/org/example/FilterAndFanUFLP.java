package org.example;

import java.util.Random;

public class FilterAndFanUFLP {

    private WarehouseLocationProblem problem;

    public FilterAndFanUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    public int[] initialSolution() {
        // Initialize a random solution
        Random random = new Random();
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

        while (improved) {
            improved = false;

            for (int j = 0; j < problem.numCustomers; j++) {
                int currentWarehouse = solution[j];
                for (int i = 0; i < problem.numWarehouses; i++) {
                    if (i != currentWarehouse) {
                        solution[j] = i;
                        double newCost = calculateCost(solution);
                        if (newCost < calculateCost(solution)) {
                            improved = true;
                        } else {
                            solution[j] = currentWarehouse;
                        }
                    }
                }
            }
        }

        return solution;
    }

    public int[] filterAndFan() {
        int[] solution = initialSolution();
        solution = localSearch(solution);
        return solution;
    }
}