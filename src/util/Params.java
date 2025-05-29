/* These are the parameters that control the model */

package util;

import java.util.Random;
import world.World;

public class Params {

    //--- Parameters that can be changed ---//
    public static final int MIN_LIFE = 10; // minimum lifespan (in ticks) 
    public static final int MAX_LIFE = 1000; // maximum lifespan (in ticks)
    public static final int NUM_PEOPLE = 250; // the number of people that always exist in the model
    public static final int PERCENT_BEST_LAND = 10; // the number of patches that are seeded with the max grain at setup
    public static final int MAX_GRAIN = 50; // the maximum number of grain that any patch could have
    public static final int GRAIN_GROWTH_INTERVAL = 10; // how long it takes for each grain to replenish (in ticks)
    public static final int MAX_METABOLISM = 15; // the maximum amount of grain consumed per tick
    public static final int MAX_VISION = 5; // the furthest possible distance that a person can see
    public static final int MAX_TICK = 7000; // the maximum tick number of a simulation
    public static final int PRINT_INTERVAL = 100; // interval for printing statistics (every 100 ticks)

    //--- Randomisers for Patch and Person classes ---//
    public static int lifeExpectancy() {
        Random random = new Random();
        return (random.nextInt(MAX_LIFE-MIN_LIFE+1)+MIN_LIFE);
    }

    public static int rollBestLand() {
        Random random = new Random();
        if ((random.nextFloat()*100)<=PERCENT_BEST_LAND) {
            return MAX_GRAIN;
        }
        return 0;
    }

    public static int rollCoord() {
        Random random = new Random();
        return random.nextInt(World.maxCoord);
        
    }

    public static int metabolism() {
        Random random = new Random();
        return 1+random.nextInt(Params.MAX_METABOLISM);
        
    }
    public static int vision() {
        Random random = new Random();
        return 1+random.nextInt(Params.MAX_VISION);
        
    }
}