/* This class is in charge of how people move within the system */

package person;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import person.strategy.ReproductionStrategy;
import util.Params;
import world.Patch;
import world.World;

public class Person{
    private int lifeExpectancy;
    private int age; 
    private int metabolism;
    private int wealth;
    private int vision;
    private Patch currentPatch;
    private World world;

    // Creation of a person will generate values to make them have unique attributes
    // Wealth isn't inherited in this system.
    public Person(World world) {
        this.world = world;
        this.lifeExpectancy = Params.lifeExpectancy();
        Random random = new Random();
        this.age = random.nextInt(this.lifeExpectancy); // even if a person is created, their actual age is still randomise
        this.metabolism = Params.metabolism();
        this.wealth = random.nextInt(50) + this.metabolism;
        this.vision = Params.vision();
    }

        // Find the patch with the most grain within vision
    private synchronized Patch findBestPatch() {
        Patch[][] map = world.getMap();
        List<Patch> visiblePatches = new ArrayList<>();
        int x = currentPatch.getXCoord();
        int y = currentPatch.getYCoord();

        // Check patches within vision distance
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

      // Check if coordinates are valid
    private boolean isValidCoord(int x, int y) {
        return x >= 0 && x < World.maxCoord && y >= 0 && y < World.maxCoord;
    }
      // Harvest grain from current patch
    private void harvest() {
        double grain = currentPatch.getGrain();
        this.wealth += (int) grain;
        currentPatch.setGrain(0);
    }

    // Consume grain based on metabolism
    private boolean metabolize() {
        this.wealth -= metabolism;
        return this.wealth >= 0;
    }

    public boolean isAliveAfterMetabolize() {
        // Metabolize and Age to check if alive
        return metabolize() && age < lifeExpectancy;
    }

    public boolean isAlive() {
        // Metabolize and Age to check if alive
        return this.wealth >= 0 && age < lifeExpectancy;
    }

    // Run method for thread-based execution
    public synchronized void tick(ReproductionStrategy strategy) {

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
    // Set the patch where the person is located
    public void setPatch(Patch patch) {
        this.currentPatch = patch;
    }

    public void setWealth(int wealth) { this.wealth = wealth; }

    public int getWealth() {
        return wealth;
    }

    public int getAge() {
        return age;
    }

    public int getMetabolism() {
        return metabolism;
    }

    public int getVision() {
        return vision;
    }

    public Patch getCurrentPatch() {
        return currentPatch;
    }
}
