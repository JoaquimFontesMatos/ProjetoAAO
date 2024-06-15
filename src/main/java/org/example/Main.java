package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
            WarehouseLocationProblem problem =new WarehouseLocationProblem("src/main/java/org/example/FicheirosTeste/ORLIB/ORLIB-uncap/130/cap131.txt");

            FilterAndFanUFLP algorithm = new FilterAndFanUFLP(problem);
            int[] solution = algorithm.filterAndFan();
            double cost = algorithm.calculateCost(solution);

            System.out.println("Optimal solution cost: " + cost);
            for (int i = 0; i < solution.length; i++) {
                System.out.println("Customer " + i + " assigned to warehouse " + solution[i]);
            }
    }
}