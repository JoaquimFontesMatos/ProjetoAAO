package org.example;

import java.util.*;

public class TabuSearchUFLP {

    private WarehouseLocationProblem problem;

    private int[] assignment;
    private double bestCost;

    private int tenure = 10;
    private int maxIterations = 100;
    private int stagnationLimit = 10;

    private Queue<Move> tabuList;

    private class Move {
        int customer;
        int fromWarehouse;
        int toWarehouse;

        Move(int customer, int fromWarehouse, int toWarehouse) {
            this.customer = customer;
            this.fromWarehouse = fromWarehouse;
            this.toWarehouse = toWarehouse;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Move move = (Move) obj;
            return customer == move.customer && fromWarehouse == move.fromWarehouse && toWarehouse == move.toWarehouse;
        }

        @Override
        public int hashCode() {
            return Objects.hash(customer, fromWarehouse, toWarehouse);
        }
    }

    public TabuSearchUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    private void initializeSolution() {
        assignment = new int[problem.numCustomers];

        Random rand = new Random(42); // Configura a semente para reprodutibilidade
        for (int j = 0; j < problem.numCustomers; j++) {
            assignment[j] = rand.nextInt(problem.numWarehouses);
        }

        bestCost = calculateCost(assignment);
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

        tabuList = new LinkedList<>();
        int iteration = 0;
        int stagnationCount = 0;
        double lastBestCost = bestCost;

        while (iteration < maxIterations && stagnationCount < stagnationLimit) {
            boolean foundBetterNeighbor = false;
            Move bestMove = null;
            double bestMoveCost = Double.POSITIVE_INFINITY;

            for (int j = 0; j < problem.numCustomers; j++) {
                int currentWarehouse = assignment[j];
                for (int i = 0; i < problem.numWarehouses; i++) {
                    if (i != currentWarehouse) {
                        Move move = new Move(j, currentWarehouse, i);

                        if (!isTabu(move)) {
                            int oldWarehouse = assignment[j];
                            assignment[j] = i;
                            double currentCost = calculateCost(assignment);

                            if (currentCost < bestCost && currentCost < bestMoveCost) {
                                bestMove = move;
                                bestMoveCost = currentCost;
                                foundBetterNeighbor = true;
                            }

                            assignment[j] = oldWarehouse;
                        }
                    }
                }
            }

            if (foundBetterNeighbor && bestMove != null) {
                Move move = bestMove;
                assignment[move.customer] = move.toWarehouse;
                bestCost = bestMoveCost;

                tabuList.offer(move);
                if (tabuList.size() > tenure) {
                    tabuList.poll();
                }

                if (bestCost < lastBestCost) {
                    lastBestCost = bestCost;
                    stagnationCount = 0;
                } else {
                    stagnationCount++;
                }
            } else {
                stagnationCount++;
            }

            iteration++;
        }

        System.out.println("Optimal solution cost (Tabu Search): " + bestCost);
        //for (int i = 0; i < assignment.length; i++) {
        // System.out.println("Customer " + i + " assigned to warehouse " + assignment[i]);
        //}
    }

    private boolean isTabu(Move move) {
        return tabuList.contains(move);
    }
}