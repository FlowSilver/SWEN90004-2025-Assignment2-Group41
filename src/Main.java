/* This is where the model will run */

import person.strategy.DefaultStrategy;
import person.strategy.InheritanceStrategy;
import person.strategy.RandomSpawnStrategy;
import world.World;

public class Main {

    public static void main(String[] args) {
        World world = new World(new InheritanceStrategy());
        world.runSimulation();
    }
}
