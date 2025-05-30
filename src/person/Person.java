/* This class is in charge of how people move within the system */

package person;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import person.strategy.ReproductionStrategy;
import util.Params;
import world.Patch;
import world.World;

/**
 * Represents a person(agent) in the simulation.
 *
 * A person has attributes such as life expectancy, age, metabolism, wealth,
 * vision, and a reference to their current patch and the world they exist in.
 * A person can move around the world, harvest resources, age, metabolize,
 * and reproduce using a given strategy.
 */
public class Person{
    private int lifeExpectancy;
    private int age; 
    private int metabolism;
    private int wealth;
    private int vision;
    private Patch currentPatch;
    private World world;

    /**
     * Constructs a person with randomly generated life expectancy, age,
     * metabolism, vision, and wealth.
     *
     * Wealth is randomly assigned and not inherited at creation without ReproductionStrategy.
     *
     * @param world the world the person exists in
     */
    public Person(World world) {
        this.world = world;
        this.lifeExpectancy = Params.lifeExpectancy();
        Random random = new Random();
        this.age = random.nextInt(this.lifeExpectancy); // even if a person is created, their actual age is still randomise
        this.metabolism = Params.metabolism();
        this.wealth = random.nextInt(50) + this.metabolism;
        this.vision = Params.vision();
    }

    /**
     * Finds the best unoccupied patch with the highest amount of grain
     * within the person's vision range.
     *
     * @return the patch with the most grain that is within reach
     */
    private synchronized Patch findBestPatch() {
        Patch[][] map = world.getMap();
        List<Patch> visiblePatches = new ArrayList<>();
        int x = currentPatch.getXCoord();
        int y = currentPatch.getYCoord();

        // Add patches within vision distance
        for (int dx = -vision; dx <= vision; dx++) {
            for (int dy = -vision; dy <= vision; dy++) {
                if (Math.abs(dx) + Math.abs(dy) <= vision) {
                    int newX = (x + dx + World.maxCoord) % World.maxCoord;
                    int newY = (y + dy + World.maxCoord) % World.maxCoord;
                    if (isValidCoord(newX, newY)) {
                        Patch patch = map[newX][newY];
                        if (patch.getPerson() == null) { // Only consider unoccupied patches
                            visiblePatches.add(patch);
                        }
                    }
                }
            }
        }

        // Find patch with maximum grain
        Patch bestPatch = currentPatch;
        double maxGrain = currentPatch.getGrain();
        for (Patch p : visiblePatches) {
            if (p.getGrain() > maxGrain) {
                maxGrain = p.getGrain();
                bestPatch = p;
            }
        }
        return bestPatch;
    }

    /**
     * Checks if the given coordinates are within valid bounds of the world.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are valid, false otherwise
     */
    private boolean isValidCoord(int x, int y) {
        return x >= 0 && x < World.maxCoord && y >= 0 && y < World.maxCoord;
    }

    /**
     * Harvests grain from the current patch and adds it to the person's wealth.
     * The patch's grain is reset to zero after harvesting.
     */
    private void harvest() {
        double grain = currentPatch.getGrain();
        this.wealth += (int) grain;
        currentPatch.setGrain(0);
    }

    /**
     * Reduces wealth based on metabolism and returns whether the person is still alive.
     *
     * @return true if the person has enough wealth after metabolizing, false otherwise
     */
    private boolean metabolize() {
        this.wealth -= metabolism;
        return this.wealth >= 0;
    }

    /**
     * Checks if the person survives after metabolizing and aging.
     *
     * @return true if the person is still alive, false otherwise
     */
    public boolean isAliveAfterMetabolizeAndAging() {
        // Metabolize and Age to check if alive
        age++;
        return metabolize() && age < lifeExpectancy;
    }

    /**
     * Executes a single simulation tick for the person.
     * The person moves to the best available patch, harvests grain,
     * and undergoes reproduction based on the given strategy.
     *
     * @param strategy the reproduction strategy used during this tick
     */
    public void tick(ReproductionStrategy strategy) {
        // Move to best patch
        Patch bestPatch = findBestPatch();
        if (bestPatch != currentPatch) {
            currentPatch.setPerson(null);
            bestPatch.setPerson(this);
            currentPatch = bestPatch;
        }

        // Harvest grain
        harvest();

        strategy.reproduce(this, world);
    }

    //--- Getter and setter functions ---//
    public void setPatch(Patch patch) {
        this.currentPatch = patch;
    }

    public void setWealth(int wealth) { this.wealth = wealth; }

    public int getWealth() {
        return wealth;
    }

    public Patch getCurrentPatch() {
        return currentPatch;
    }
}
