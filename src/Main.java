/* This is where the model will run */

import world.Simulation;

/**
 * Main class to run the simulation model.
 *
 * This class contains the main method which creates an instance of the Simulation
 * class and runs different simulations with varying parameters including
 * grain growth rate, population size, and reproduction rules.
 */
public class Main {

    /**
     * The entry point of the simulation program.
     * It runs simulations for various grain growth rates, population sizes,
     * and reproduction strategies.
     */
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.simulateVariousGrowthRate();
        simulation.simulateVariousPopulation();
        simulation.simulateVariousReproductionRule();
    }

}
