/* This is where the model will run */

import person.strategy.DefaultStrategy;
import person.strategy.InheritanceStrategy;
import person.strategy.RandomSpawnStrategy;
import util.Params;
import world.World;

public class Main {

    public static void main(String[] args) {
        World world = new World(Params.NUM_PEOPLE, Params.GRAIN_GROWTH_INTERVAL,
                new InheritanceStrategy());
        world.runSimulation();
    }
}
