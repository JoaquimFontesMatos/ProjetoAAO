package org.example;

import java.util.Random;

public class LocalSearchUFLP {

    private WarehouseLocationProblem problem;
    private int[] assignment;
    private double bestCost;

    public LocalSearchUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    private void initializeSolution() {
        assignment = new int[problem.numCustomers];

        Random rand = new Random();
        for (int j = 0; j < problem.numCustomers; j++) {
            assignment[j] = rand.nextInt(problem.numWarehouses);
        }

        bestCost = calculateCost(assignment);
    }

    public double getBestCost() {
        return bestCost;
    }

    private double calculateCost(int[] assignment) {
        double totalCost = 0.0;
        int[] warehouseUsage = new int[problem.numWarehouses];

        for (int j = 0; j < problem.numCustomers; j++) {
            int warehouse = assignment[j];
            warehouseUsage[warehouse] += problem.demands[j];
            totalCost += problem.allocationCosts[warehouse][j];
        }

        for (int i = 0; i < problem.numWarehouses; i++) {
            if (warehouseUsage[i] > 0) {
                totalCost += problem.fixedCosts[i];
            }
        }

        return totalCost;
    }

    public void solve() {
        initializeSolution();

        int maxIterations = 200;
        int iteration = 0;
        int stagnationCount = 0;
        int maxStagnation = 10;
        int maxResets = 3;
        int resetCount = 0;
        double lastBestCost = bestCost;

        while (iteration < maxIterations && resetCount < maxResets) {
            boolean improved = false;

            for (int j = 0; j < problem.numCustomers; j++) {
                int currentWarehouse = assignment[j];
                for (int i = 0; i < problem.numWarehouses; i++) {
                    if (i != currentWarehouse) {
                        assignment[j] = i;
                        double currentCost = calculateCost(assignment);
                        if (currentCost < bestCost) {
                            bestCost = currentCost;
                            improved = true;
                        } else {
                            assignment[j] = currentWarehouse;
                        }
                    }
                }
            }

            if (improved) {
                lastBestCost = bestCost;
                stagnationCount = 0;
            } else {
                stagnationCount++;
                if (stagnationCount >= maxStagnation) {
                    initializeSolution();
                    stagnationCount = 0;
                    resetCount++;
                }
            }

            iteration++;
        }

        //System.out.println("Optimal solution cost (Local Search): " + bestCost);
        //for (int i = 0; i < assignment.length; i++) {
            //System.out.println("Customer " + i + " assigned to warehouse " + assignment[i]);
       // }
    }
}