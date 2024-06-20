package org.example;

import java.util.Random;

/**
 * A classe FilterAndFanUFLP implementa uma abordagem metaheurística para
 * resolver o Problema de Localização de Armazéns Não Capacitados (UFLP).
 * Utiliza uma solução inicial aleatória seguida de uma busca local e uma
 * técnica de filtro e expansão para encontrar soluções quase ótimas.
 */
public class FilterAndFanUFLP {

    private WarehouseLocationProblem problem;

    /**
     * Construtor da classe
     * 
     * @param problem Uma instância da classe WarehouseLocationProblem, que contêm
     *                os
     *                dados do problema, como o número de clientes, o número de
     *                armazéns, as demandas, os custos fixos e os custos de
     *                alocação.
     */
    public FilterAndFanUFLP(WarehouseLocationProblem problem) {
        this.problem = problem;
    }

    /**
     * Gera uma solução inicial aleatória.
     * 
     * @return Um array que representa a atribuição inicial de clientes a armazéns.
     */
    public int[] initialSolution() {
        Random random = new Random(42);
        int[] solution = new int[problem.numCustomers];
        for (int j = 0; j < problem.numCustomers; j++) {
            solution[j] = random.nextInt(problem.numWarehouses);
        }
        return solution;
    }

    /**
     * Calcula o custo total de uma determinada solução.
     * 
     * @param solution Um array que representa a atribuição de clientes a armazéns.
     * @return O custo total da solução.
     */
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

    /**
     * Realiza uma pesquisa local para melhorar a solução inicial, explorando soluções
     * vizinhas.
     * 
     * @param solution Um array que representa a atribuição inicial de clientes a
     *                 armazéns.
     * @return Uma solução melhorada após realizar a pesquisa local.
     */
    public int[] localSearch(int[] solution) {
        boolean improved = true;
        int iteration = 0;
        double currentCost = calculateCost(solution);

        while (improved && iteration < 100) {
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

    /**
     * Aplica a metaheurística de filtro e expansão, que inclui a geração de uma
     * solução inicial, pesquisa local e passos de perturbação.
     * 
     * @return Uma solução obtida após aplicar a abordagem de filtro e expansão.
     */
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

    /**
     * Perturba a solução dada, reatribuindo aleatoriamente alguns clientes a
     * diferentes armazéns.
     * 
     * @param solution Um array que representa a atribuição atual de clientes a
     *                 armazéns.
     * @return Uma solução perturbada.
     */
    private int[] perturbSolution(int[] solution) {
        Random random = new Random(42);

        for (int j = 0; j < problem.numCustomers; j++) {
            if (random.nextDouble() < 0.1) {
                solution[j] = random.nextInt(problem.numWarehouses);
            }
        }

        return solution;
    }
}
