package org.example;

import java.util.*;

public class TabuSearchUFLP {

    private WarehouseLocationProblem problem; // Instância do problema de localização de armazéns.
    private int[] assignment; // Array que armazena a atribuição de clientes para armazéns.
    private double bestCost; // Custo da melhor solução encontrada até agora.

    private int tenure = 10; // Tenura da lista tabu, ou seja, o número máximo de iterações que um movimento permanece na lista tabu.
    private int maxIterations = 100; // Número máximo de iterações do algoritmo.
    private int stagnationLimit = 10; // Limite de iterações sem melhora para interromper a pesquisa.

    private Queue<Move> tabuList; // Lista tabu que armazena os movimentos proibidos temporariamente.

    /**
     * Classe interna que representa um movimento (realocação de um cliente de um armazém para outro).
     */
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
    
    /**
     * Construtor que recebe uma instância do problema de localização de armazéns.
     * @param problem
     */
    public TabuSearchUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    /**
     * Método para inicializar uma solução aleatória.
     */
    private void initializeSolution() {
        assignment = new int[problem.numCustomers];
        Random rand = new Random(42); // Configura a semente para reprodutibilidade
        for (int j = 0; j < problem.numCustomers; j++) {
            assignment[j] = rand.nextInt(problem.numWarehouses);
        }
        bestCost = calculateCost(assignment);
    }

    /**
     * Método para calcular o custo total da solução dada uma atribuição de clientes para armazéns.
     * @param assignment
     * @return
     */
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


    /**
     * Método para obter o custo da melhor solução encontrada.
     * @return
     */
    public double getBestCost() {
        return bestCost;
    }

    /**
     * Método que executa o algoritmo de pesquisa tabu.
     */
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
            // Avalia todos os possíveis movimentos.
            for (int j = 0; j < problem.numCustomers; j++) {
                int currentWarehouse = assignment[j];
                for (int i = 0; i < problem.numWarehouses; i++) {
                    if (i != currentWarehouse) {
                        Move move = new Move(j, currentWarehouse, i);
                        // Verifica se o movimento é tabu ou se satisfaz os critérios de aspiração.
                        if (!isTabu(move) || satisfiesAspirationCriteria(move, bestMoveCost)) {
                            int oldWarehouse = assignment[j];
                            assignment[j] = i;
                            double currentCost = calculateCost(assignment);
                            // Atualiza o melhor movimento encontrado.
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
            // Executa o melhor movimento encontrado.
            if (foundBetterNeighbor && bestMove != null) {
                assignment[bestMove.customer] = bestMove.toWarehouse;
                bestCost = bestMoveCost;
                // Adiciona o movimento à lista tabu.
                tabuList.offer(bestMove);
                if (tabuList.size() > tenure) {
                    tabuList.poll();
                }
                // Verifica se houve melhora no custo da melhor solução.
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

        //System.out.println("Optimal solution cost (Tabu Search): " + bestCost);
    }

    /**
     * Método que verifica se um movimento está na lista tabu.
     * @param move
     * @return
     */
    private boolean isTabu(Move move) {
        return tabuList.contains(move);
    }

    /**
     * Método que verifica se um movimento satisfaz os critérios de aspiração.
     * @param move
     * @param moveCost
     * @return
     */
    private boolean satisfiesAspirationCriteria(Move move, double moveCost) {
        return moveCost < bestCost;
    }
}
