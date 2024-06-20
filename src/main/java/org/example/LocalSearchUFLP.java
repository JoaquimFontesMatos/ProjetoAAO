package org.example;

import java.util.Random;

public class LocalSearchUFLP {

    private WarehouseLocationProblem problem;
    private int[] assignment;
    private double bestCost;
    private int[] bestAssignment;
    private int[] warehouseUsage;

    public LocalSearchUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    private void initializeSolution() {
        assignment = new int[problem.numCustomers];
        bestAssignment = new int[problem.numCustomers];
        warehouseUsage = new int[problem.numWarehouses];

        Random rand = new Random(42);
        for (int j = 0; j < problem.numCustomers; j++) {
            assignment[j] = rand.nextInt(problem.numWarehouses);
        }

        bestCost = calculateCost(assignment);
        System.arraycopy(assignment, 0, bestAssignment, 0, assignment.length);
    }

    public double getBestCost() {
        return bestCost;
    }

    private double calculateCost(int[] assignment) {
        double totalCost = 0.0;

        // Reset warehouse usage
        for (int i = 0; i < warehouseUsage.length; i++) {
            warehouseUsage[i] = 0;
        }

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

    private double calculateIncrementalCost(int customer, int oldWarehouse, int newWarehouse, int[] warehouseUsage) {
        double incrementalCost = 0.0;
        int demand = problem.demands[customer];

        // Remove cost associated with the old warehouse
        incrementalCost -= problem.allocationCosts[oldWarehouse][customer];
        if (warehouseUsage[oldWarehouse] == demand) {
            incrementalCost -= problem.fixedCosts[oldWarehouse];
        }

        // Add cost associated with the new warehouse
        incrementalCost += problem.allocationCosts[newWarehouse][customer];
        if (warehouseUsage[newWarehouse] == 0) {
            incrementalCost += problem.fixedCosts[newWarehouse];
        }

        return incrementalCost;
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
                        double incrementalCost = calculateIncrementalCost(j, currentWarehouse, i, warehouseUsage);
                        if (incrementalCost < 0) {
                            bestCost += incrementalCost;
                            warehouseUsage[currentWarehouse] -= problem.demands[j];
                            warehouseUsage[i] += problem.demands[j];
                            assignment[j] = i;
                            improved = true;
                        }
                    }
                }
            }

            if (improved) {
                lastBestCost = bestCost;
                stagnationCount = 0;
                System.arraycopy(assignment, 0, bestAssignment, 0, assignment.length);
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

        bestCost = calculateCost(bestAssignment);
        //System.out.println("Optimal solution cost (Local Search): " + bestCost);
        //for (int i = 0; i < bestAssignment.length; i++) {
        //    System.out.println("Customer " + i + " assigned to warehouse " + bestAssignment[i]);
        //}
    }
}
