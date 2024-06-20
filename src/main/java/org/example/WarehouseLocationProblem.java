package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WarehouseLocationProblem {
    int numWarehouses; // Número de armazéns.
    int numCustomers; // Número de clientes.
    double[] capacities; // Capacidades dos armazéns.
    float[] fixedCosts; // Custos fixos de abertura dos armazéns.
    int[] demands; // Demandas dos clientes.
    float[][] allocationCosts; // Custos de alocação de clientes para armazéns.

    // Construtor que recebe o caminho do arquivo com os dados do problema.
    public WarehouseLocationProblem(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath)); // Abre o arquivo para leitura.
            numWarehouses = scanner.nextInt(); // Lê o número de armazéns.
            numCustomers = scanner.nextInt(); // Lê o número de clientes.

            this.capacities = new double[numWarehouses]; // Inicializa o array de capacidades.
            this.fixedCosts = new float[numCustomers]; // Inicializa o array de custos fixos.
            this.demands = new int[numCustomers]; // Inicializa o array de demandas.
            this.allocationCosts = new float[numWarehouses][numCustomers]; // Inicializa a matriz de custos de alocação.

            // Exibe o número de armazéns e clientes para verificação
            System.out.println("numFacilities: " + numWarehouses);
            System.out.println("numCustomers: " + numCustomers);

            capacities = new double[numWarehouses]; // Inicializa novamente (redundante).
            fixedCosts = new float[numWarehouses]; // Corrigido para numWarehouses em vez de numCustomers.
            demands = new int[numCustomers]; // Inicializa novamente (redundante).
            allocationCosts = new float[numWarehouses][numCustomers]; // Inicializa novamente (redundante).

            // Lê as capacidades e custos fixos dos armazéns.
            for (int i = 0; i < numWarehouses; i++) {
                capacities[i] = scanner.nextDouble(); // Lê a capacidade do armazém i.
                // Lê a linha seguinte que contém o custo fixo do armazém i.
                String input = scanner.nextLine();
                fixedCosts[i] = Float.parseFloat(input.trim()); // Converte e armazena o custo fixo.
            }

            // Lê as demandas dos clientes e os custos de alocação.
            for (int i = 0; i < numCustomers; i++) {
                demands[i] = scanner.nextInt(); // Lê a demanda do cliente i.

                // Lê os custos de alocação do cliente i para cada armazém.
                for (int j = 0; j < numWarehouses; j++) {
                    String input = scanner.next();
                    allocationCosts[j][i] = Float.parseFloat(input.trim()); // Converte e armazena o custo de alocação.
                }
            }
            scanner.close(); // Fecha o scanner após a leitura do arquivo.
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao carregar o arquivo de dados: " + e.getMessage());
        }
    }
}