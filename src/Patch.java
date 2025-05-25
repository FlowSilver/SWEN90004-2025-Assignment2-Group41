package src;
import java.lang.Math;
import java.util.*;

public class Patch extends Thread {
    private double grain;
    private double maxGrain;
    private int xCoord;
    private int yCoord;
    private Person person;

    // Used for finding all valid coordinates
    private static int[][] dirs = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1}, {0, 1},
        {1, -1}, {1, 0}, {1, 1}
    };

    // Upon creation, the patch is randomly assigned to either be
    // A normal patch or to have max grain
    public Patch(int xCoord, int yCoord) {
        this.maxGrain = Params.rollBestLand();
        this.grain = this.maxGrain;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    //--- Setup functions ---//

    // DELETE WHEN CONSIDERED: For determining movement of person, I recommend using the logic found here
    // function used to spread grain through to adjacent patches by taking a quarter of the grain from
    // a given patch and distributing it.
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

    // Checks if coordinate is on the map
    private boolean isValidCoord(int x, int y) {
        if (x < 0 || y < 0 || x >= World.maxCoord || y >= World.maxCoord) {
            return false;
        }
        return true;
    }

    // Rounds down to the nearest grain integer
    public void floorGrain() {
        this.setGrain(Math.floor(grain));
    }
    
    //--- Functions while model is running ---//

    public void replenish() {
        this.grain = this.maxGrain;
    }

    public void run() {
        while(!this.isInterrupted()) {
            try {
                sleep(Params.GRAIN_GROWTH_INTERVAL);
            } catch (InterruptedException e) {}
        } 
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
}

