package org.example;

import java.util.*;

public class LocalSearchUFLP {

    // Variáveis do problema UFLP
    private int numFacilities;  // número de instalações
    private int numCustomers;   // número de clientes
    private double[][] cost;    // matriz de custos

    // Solução do Local Search
    private boolean[] isOpen;   // vetor indicando se a instalação está aberta
    private int[] assignment;   // vetor de atribuição de clientes a instalações
    private double bestCost;    // melhor custo encontrado

    // Construtor
    public LocalSearchUFLP(int numFacilities, int numCustomers, double[][] cost) {
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

        // Garantir que pelo menos uma instalação esteja aberta
        boolean anyOpen = false;
        for (boolean open : isOpen) {
            if (open) {
                anyOpen = true;
                break;
            }
        }
        if (!anyOpen) {
            isOpen[rand.nextInt(numFacilities)] = true;
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
                // Penalidade menor se o cliente for atribuído a uma instalação fechada
                totalCost += 1e3; // valor menor para permitir mais flexibilidade
            }
        }
        return totalCost;
    }

    // Executa o Local Search para encontrar uma solução melhor
    public void solve() {
        initializeSolution();

        // Parâmetros do Local Search
        int maxIterations = 200;  // Número máximo de iterações
        int iteration = 0;
        int stagnationCount = 0;
        int maxStagnation = 10; // Permitir menos estagnações antes da reinicialização
        int maxResets = 3; // Limitar o número de reinicializações permitidas
        int resetCount = 0;
        double lastBestCost = bestCost;

        while (iteration < maxIterations && resetCount < maxResets) {
            boolean improved = false;

            // Exploração de vizinhança: tenta trocar a atribuição de um cliente
            for (int j = 0; j < numCustomers; j++) {
                int currentFacility = assignment[j];
                for (int i = 0; i < numFacilities; i++) {
                    if (i != currentFacility && isOpen[i]) {
                        // Tenta atribuir o cliente j à instalação i e avalia o custo
                        assignment[j] = i;
                        double currentCost = calculateCost(isOpen, assignment);
                        if (currentCost < bestCost) {
                            bestCost = currentCost;
                            improved = true;
                        } else {
                            // Reverte a atribuição
                            assignment[j] = currentFacility;
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
                    System.out.println("Estagnação atingida. Reinicializando a solução.");
                    initializeSolution();
                    stagnationCount = 0;
                    resetCount++;
                }
            }

            iteration++;
        }

        // Exibir resultado final do Local Search
        System.out.println("Melhor custo encontrado para o LocalSearch: " + bestCost);
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
}
