/* These are the parameters that control the model */

package util;

import java.util.Random;
import world.World;

public class Params {

    //--- Parameters that can be changed ---//
    public static final int MIN_LIFE = 1; // minimum lifespan (in ticks)
    public static final int MAX_LIFE = 83; // maximum lifespan (in ticks)
    public static final int NUM_PEOPLE = 250; // the number of people that always exist in the model
    public static final int PERCENT_BEST_LAND = 10; // the number of patches that are seeded with the max grain at setup
    public static final int MAX_GRAIN = 50; // the maximum number of grain that any patch could have
    public static final int GRAIN_GROWTH_INTERVAL = 1; // how long it takes for each grain to replenish (in ticks)
    public static final int MAX_METABOLISM = 15; // the maximum amount of grain consumed per tick
    public static final int MAX_VISION = 5; // the furthest possible distance that a person can see
    public static final int PRINT_WRITE_INTERVAL = 10; // interval for printing statistics (every 100 ticks)
    public static final int NUM_GROW = 4; // Number of Grain that grows each time

    // When changing this, also changing tick_interval in process_results.py
    public static final int MAX_TICK = 1000; // the maximum tick number of a simulation.

    //--- Randomizers for Patch and Person classes ---//

    /**
     * Generates a random life expectancy value between MIN_LIFE and MAX_LIFE.
     * @return a randomly generated life expectancy value
     */
    public static int lifeExpectancy() {
        Random random = new Random();
        return (random.nextInt(MAX_LIFE-MIN_LIFE+1)+MIN_LIFE);
    }

    /**
     * Determines if a patch should be initialized as best land (with maximum grain),
     * based on PERCENT_BEST_LAND probability.
     * @return MAX_GRAIN if selected as best land, otherwise 0
     */
    public static int rollBestLand() {
        Random random = new Random();
        if ((random.nextFloat()*100)<=PERCENT_BEST_LAND) {
            return MAX_GRAIN;
        }
        return 0;
    }

    /**
     * Generates a random coordinate value within the bounds of the world.
     * @return a random coordinate value within the world
     */
    public static int rollCoord() {
        Random random = new Random();
        return random.nextInt(World.maxCoord);
        
    }

    /**
     * Generates a random metabolism value between 1 and MAX_METABOLISM.
     * @return a randomly generated metabolism value
     */
    public static int metabolism() {
        Random random = new Random();
        return 1+random.nextInt(Params.MAX_METABOLISM);
        
    }

    /**
     * Generates a random vision value between 1 and MAX_VISION.
     * @return a randomly generated vision value
     */
    public static int vision() {
        Random random = new Random();
        return 1+random.nextInt(Params.MAX_VISION);
        
    }
}