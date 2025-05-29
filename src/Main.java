/* This is where the model will run */

import world.Simulation;

public class Main {

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.simulateVariousGrowthRate();
        simulation.simulateVariousPopulation();
        simulation.simulateVariousReproductionRule();
    }

}
