package org.example;

import java.util.*;

public class TabuSearchUFLP {

    // Variáveis do problema UFLP
    private int numFacilities;  // número de instalações
    private int numCustomers;   // número de clientes
    private double[][] cost;    // matriz de custos

    // Solução do Tabu Search
    private boolean[] isOpen;   // vetor indicando se a instalação está aberta
    private int[] assignment;   // vetor de atribuição de clientes a instalações
    private double bestCost;    // melhor custo encontrado

    // Parâmetros do Tabu Search
    private int tenure = 10;    // tamanho da lista Tabu
    private int maxIterations = 100;  // Número máximo de iterações
    private int stagnationLimit = 10; // Limite de iterações sem melhoria para considerar estagnação

    // Lista Tabu para guardar movimentos recentes
    private Queue<Move> tabuList;

    // Estrutura para representar um movimento (troca de cliente)
    private class Move {
        int customer;
        int fromFacility;
        int toFacility;

        Move(int customer, int fromFacility, int toFacility) {
            this.customer = customer;
            this.fromFacility = fromFacility;
            this.toFacility = toFacility;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Move move = (Move) obj;
            return customer == move.customer && fromFacility == move.fromFacility && toFacility == move.toFacility;
        }

        @Override
        public int hashCode() {
            return Objects.hash(customer, fromFacility, toFacility);
        }
    }

    // Construtor
    public TabuSearchUFLP(int numFacilities, int numCustomers, double[][] cost) {
        this.numFacilities = numFacilities;
        this.numCustomers = numCustomers;
        this.cost = cost;
    }

    // Inicializa a solução inicial aleatoriamente
    private void initializeSolution() {
        isOpen = new boolean[numFacilities];
        assignment = new int[numCustomers];

        Random rand = new Random();

        // Inicializar isOpen aleatoriamente
        for (int i = 0; i < numFacilities; i++) {
            isOpen[i] = rand.nextBoolean();
        }

        // Inicializar assignment aleatoriamente, garantindo atribuição a instalações abertas
        for (int j = 0; j < numCustomers; j++) {
            int facility;
            do {
                facility = rand.nextInt(numFacilities);
            } while (!isOpen[facility]);
            assignment[j] = facility;
        }

        bestCost = calculateCost(isOpen, assignment);
    }

    // Calcula o custo total de uma solução
    private double calculateCost(boolean[] isOpen, int[] assignment) {
        double totalCost = 0.0;
        for (int j = 0; j < numCustomers; j++) {
            int facility = assignment[j];
            if (isOpen[facility]) {
                totalCost += cost[facility][j];
            } else {
                // Penalidade alta se o cliente for atribuído a uma instalação fechada
                totalCost += 1e6; // valor grande mas não extremo
            }
        }
        return totalCost;
    }

    // Implementação do algoritmo Tabu Search
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

            // Gerar vizinhança
            for (int j = 0; j < numCustomers; j++) {
                int currentFacility = assignment[j];
                for (int i = 0; i < numFacilities; i++) {
                    if (i != currentFacility && isOpen[i]) {
                        Move move = new Move(j, currentFacility, i);

                        // Verificar se o movimento é Tabu
                        if (!isTabu(move)) {
                            // Fazer o movimento
                            int oldFacility = assignment[j];
                            assignment[j] = i;
                            double currentCost = calculateCost(isOpen, assignment);

                            // Aceitar o movimento se ele melhora o custo
                            if (currentCost < bestCost && currentCost < bestMoveCost) {
                                bestMove = move;
                                bestMoveCost = currentCost;
                                foundBetterNeighbor = true;
                            }

                            // Reverter o movimento
                            assignment[j] = oldFacility;
                        }
                    }
                }
            }

            // Aplicar o melhor movimento encontrado
            if (foundBetterNeighbor && bestMove != null) {
                Move move = bestMove;
                assignment[move.customer] = move.toFacility;
                bestCost = bestMoveCost;

                // Adicionar movimento à lista Tabu
                tabuList.offer(move);
                if (tabuList.size() > tenure) {
                    tabuList.poll(); // Remove o movimento mais antigo
                }

                // Reduzir a contagem de estagnação se houve melhoria
                if (bestCost < lastBestCost) {
                    lastBestCost = bestCost;
                    stagnationCount = 0;
                } else {
                    stagnationCount++;
                }
            } else {
                // Se não houve melhoria, incrementar a contagem de estagnação
                stagnationCount++;
            }

            iteration++;
        }

        // Exibir resultado final do Tabu Search
        System.out.println("Melhor custo encontrado para o Tabu Search: " + bestCost);
        System.out.println("Instalações abertas:");
        for (int i = 0; i < numFacilities; i++) {
            if (isOpen[i]) {
                System.out.println("Instalação " + i + " está aberta.");
            }
        }
        System.out.println("Atribuição de clientes:");
        for (int j = 0; j < numCustomers; j++) {
            System.out.println("Cliente " + j + " está atribuído à instalação " + assignment[j]);
        }
    }

    // Verifica se um movimento é Tabu
    private boolean isTabu(Move move) {
        return tabuList.contains(move);
    }
}