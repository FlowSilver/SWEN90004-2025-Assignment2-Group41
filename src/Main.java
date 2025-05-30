/* This is where the model will run */

import person.strategy.DefaultStrategy;
import util.Params;
import world.Patch;
import world.Simulation;
import world.World;

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
        // Comment out if only wish to see simulation results
        Simulation simulation = new Simulation();
        simulation.simulateVariousGrowthRate();
        simulation.simulateVariousPopulation();
        simulation.simulateVariousReproductionRule();

        // Only Simulation, do not write results to csv files
//        World world = new World(Params.NUM_PEOPLE, Params.GRAIN_GROWTH_INTERVAL, new DefaultStrategy());
//        world.runSimulation();
    }

}
