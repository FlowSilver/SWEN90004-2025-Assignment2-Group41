package world;

import person.strategy.DefaultStrategy;
import person.strategy.InheritanceStrategy;
import person.strategy.RandomSpawnStrategy;
import person.strategy.ReproductionStrategy;
import util.Params;

import java.util.ArrayList;
import java.util.List;

/**
 * The Simulation class runs multiple simulations of the world model
 * varying different parameters such as population size, reproduction strategy,
 * and grain growth rate. Each simulation is repeated multiple times to gather data.
 */
public class Simulation {

    /**
     * Runs simulations for different population sizes.
     * For each population size, runs 10 simulations with a default reproduction strategy
     * and writes results to a CSV file named based on the population.
     */
    public void simulateVariousPopulation() {
        List<Integer> populationList= new ArrayList<>();
        populationList.add(100);
        populationList.add(Params.NUM_PEOPLE); //250
        populationList.add(500);
        populationList.add(1000);

        for(Integer i: populationList) {
            for (int j = 0; j < 10; j++) {
                World world = new WorldWriter(i, Params.GRAIN_GROWTH_INTERVAL, new DefaultStrategy(), "Population_" + i + ".csv");
                world.runSimulation();
            }
        }
    }

    /**
     * Runs simulations using different reproduction strategies.
     * For each reproduction strategy, runs 10 simulations with default population size and grain growth interval,
     * writing results to a CSV file named after the strategy.
     */
    public void simulateVariousReproductionRule() {
        List<ReproductionStrategy> strategies= new ArrayList<>();
        strategies.add(new RandomSpawnStrategy());
        strategies.add(new InheritanceStrategy());
        strategies.add(new DefaultStrategy());

        for(ReproductionStrategy strategy: strategies) {
            for (int j = 0; j < 10; j++) {
                World world = new WorldWriter(Params.NUM_PEOPLE, Params.GRAIN_GROWTH_INTERVAL,
                        strategy, strategy + ".csv");
                world.runSimulation();
            }
        }
    }

    /**
     * Runs simulations for different grain growth intervals.
     * For each growth rate, runs 10 simulations with default population and reproduction strategy,
     * writing results to a CSV file named based on the growth rate.
     */
    public void simulateVariousGrowthRate() {
        List<Integer> growthRateList= new ArrayList<>();
        growthRateList.add(1);
        growthRateList.add(Params.GRAIN_GROWTH_INTERVAL); //10
        growthRateList.add(50);
        growthRateList.add(100);

        for(Integer i: growthRateList) {
            for (int j = 0; j < 10; j++) {
                World world = new WorldWriter(Params.NUM_PEOPLE, i, new DefaultStrategy(), "GrowthRate_" + i + ".csv");
                world.runSimulation();
            }
        }
    }
}
