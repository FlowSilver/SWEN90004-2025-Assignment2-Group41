package world;

import person.strategy.DefaultStrategy;
import person.strategy.InheritanceStrategy;
import person.strategy.RandomSpawnStrategy;
import person.strategy.ReproductionStrategy;
import util.Params;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

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

    public void simulateVariousGrowthRate() {
        List<Integer> growthRateList= new ArrayList<>();
        growthRateList.add(1);
        growthRateList.add(Params.GRAIN_GROWTH_INTERVAL); //10
        growthRateList.add(100);
        growthRateList.add(200);

        for(Integer i: growthRateList) {
            for (int j = 0; j < 10; j++) {
                World world = new WorldWriter(Params.NUM_PEOPLE, i, new DefaultStrategy(), "GrowthRate_" + i + ".csv");
                world.runSimulation();
            }
        }
    }
}
