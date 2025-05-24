/* This is the class that simulates the model.
 * It mainly controls the patches and their coordinates.
 * People are also created here.
 */

package src;

public class World {
    public static final int maxCoord = 51; //not a parameter; determines max size of map
    private Patch[][] map; 

    // This is synonymous to the setup phase in the NetLogo version of the program.
    public World() {
        setupPatches();
        setupPeople();
    }

    //--- Setup functions ---//

    // DELETE WHEN CONSIDERED: you may want to add a people list if you want easy access to them, for example if you do this non concurrently

    // Creates all the people and places them randomly on the map
    public void setupPeople() {
        for (int i=0; i<Params.NUM_PEOPLE; i++) {
            Patch patch = map[Params.rollCoord()][Params.rollCoord()];
            if (patch.getPerson() == null) {
                patch.setPerson(new Person());
            }
        }
    }

    // Creates and populates the world with patches of various levels of productivity
    public void setupPatches() {

        // Initial creation of patches
        this.map = new Patch[maxCoord][maxCoord];
        for (int x = 0; x<maxCoord; x++) {
            for (int y = 0; y<maxCoord; y++) {
                map[x][y] = new Patch(x, y);
            }
        }

        // Spreads the grain around the map tops up the best grain patches
        for (int k=0; k<5; k++) {
            for (int i = 0; i<maxCoord; i++) {
                for (int j = 0; j<maxCoord; j++) {
                    if (map[i][j].getMaxGrain() != 0) {
                        map[i][j].setGrain(map[i][j].getGrain());
                    }
                }
            }
            diffusePatches();
        }

        // Continue to spread grain around without topping up the original supply
        for (int k=0; k<10; k++) {
            diffusePatches();
        }

        // Sets the current grain amount as the maximum for each patch
        for (int i = 0; i<maxCoord; i++) {
            for (int j = 0; j<maxCoord; j++) {
                map[i][j].floorGrain();
                map[i][j].setMaxGrain();
            }
        }  
    }
    
    // Causes diffusal in all grains in the map
    public void diffusePatches() {
        for (int i = 0; i<maxCoord; i++) {
            for (int j = 0; j<maxCoord; j++) {
                map[i][j].diffuse(map);
            }
        }
    }


    //--- Getter and setter functions ---//

    public Patch[][] getMap() {
        return map;
    }

    public void setMap(Patch[][] map) {
        this.map = map;
    }

}