package world;
import java.util.ArrayList;
import java.util.List;
import person.Person;
import util.Params;

/**
 * The Patch class represents a unit of land in the simulation world.
 * Each patch may contain a certain amount of grain and may be occupied by a Person.
 * Grain can be diffused to neighboring patches, and patches can be replenished to their max grain.
 */
public class Patch{
    private double grain;
    private double maxGrain;
    private int xCoord;
    private int yCoord;
    private Person person;


    // All possible directions to adjacent patches (including diagonals)
    // Used for finding all valid coordinates
    private static int[][] dirs = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1}, {0, 1},
        {1, -1}, {1, 0}, {1, 1}
    };

    /**
     * Constructs a Patch at the specified coordinates.
     * The patch may be initialized with maximum grain based on model parameters.
     * @param xCoord the x-coordinate of the patch
     * @param yCoord the y-coordinate of the patch
     */
    public Patch(int xCoord, int yCoord) {
        this.maxGrain = Params.rollBestLand();
        this.grain = this.maxGrain;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    //--- Setup functions ---//

    /**
     * Distributes a quarter of the grain to all valid neighboring patches equally.
     * If some neighbors are invalid, their share of the grain returns to the original patch.
     * @param map the 2D array of patches representing the simulation world
     */
    public void diffuse(Patch[][] map) {

        // Iterates through all possible adjacent patches and adds the valid coordinates to the list
        if (grain != 0) {
            List<Patch> validSet = new ArrayList<>();
            double diffuseNum = grain/4;
            double share = diffuseNum/8; // diffusal takes the shared fraction and shares it evenly between all coordinates
            this.grain = this.grain-diffuseNum;
            
            // finds all valid coordinates
            for (int[] dir : dirs) {
                int x = xCoord + dir[0];
                int y = yCoord + dir[1];
                if (isValidCoord(x, y)) {
                    validSet.add(map[x][y]);
                }
            }

            // updates grain amount and makes sure it does not exceed the maximum
            for (Patch coord : validSet) {
                double current = coord.getGrain();
                coord.setGrain(Math.min(Params.MAX_GRAIN, current+share));
            }

            // if there are less than 8 valid neighbours, the excess shares go back to the original patch
            this.grain = this.grain + (8-validSet.size())*share;
        }
    }


    /**
     * Checks if the given coordinates are within the bounds of the world.
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the coordinates are valid; false otherwise
     */
    private boolean isValidCoord(int x, int y) {
        if (x < 0 || y < 0 || x >= World.maxCoord || y >= World.maxCoord) {
            return false;
        }
        return true;
    }

    /**
     * Rounds the grain value down to the nearest whole number.
     */
    public void floorGrain() {
        this.setGrain(Math.floor(grain));
    }
    
    //--- Functions while model is running ---//

    /**
     * Replenishes the patch to its maximum grain level.
     * This method is synchronized to ensure thread safety.
     */
    public void replenish() {
        this.grain = this.maxGrain;
    }

    //--- Getter and setter functions ---//
    public double getGrain() {
        return grain;
    }

    public void setGrain(double grain) {
        this.grain = grain;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setMaxGrain() {
        this.maxGrain = this.grain;
    }

    public double getMaxGrain() {
        return maxGrain;
    }

    public int getXCoord() {
            return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }
}

