/* This is the class that simulates the model.
 * It mainly controls the patches and their coordinates.
 * People are also created here.
 */

package src;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static final int maxCoord = 51; //not a parameter; determines max size of map
    private Patch[][] map; 
    private List<Person> people;

    // This is synonymous to the setup phase in the NetLogo version of the program.
    public World() {
        people = new ArrayList<>();
        setupPatches();
        setupPeople();
    }

    //--- Setup functions ---//

    // DELETE WHEN CONSIDERED: you may want to add a people list if you want easy access to them, for example if you do this non concurrently

    // Creates all the people and places them randomly on the map
    public void setupPeople() {
        people.clear();
        for (int i = 0; i < Params.NUM_PEOPLE; i++) {
            int x, y;
            Patch patch;

            // This gurantees that the new person will be added
            do {
                x = Params.rollCoord();
                y = Params.rollCoord();
                patch = map[x][y];
            } while (patch.getPerson() != null);
            Person person = new Person(this);
            person.setPatch(patch);
            patch.setPerson(person);
            people.add(person);
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

    // Reproduction rule (Replace dead agents)
    private synchronized void processReproduction() {
        // Reproduction and Population Maintenance (Replace dead agents)
        this.people.removeIf(p -> !p.isAlive());
        while (this.people.size() < Params.NUM_PEOPLE) {
            int x, y;
            Patch patch;
            do {
                x = Params.rollCoord();
                y = Params.rollCoord();
                patch = map[x][y];
            } while (patch.getPerson() != null);
            Person person = new Person(this);
            person.setPatch(patch);
            patch.setPerson(person);
            this.people.add(person);
            person.start();
        }
                

    }

    private synchronized void printStatistics(int tick) {
        double totalWealth = people.stream().mapToDouble(Person::getWealth).sum();
        double minWealth = people.stream().mapToDouble(Person::getWealth).min().orElse(0);
        double maxWealth = people.stream().mapToDouble(Person::getWealth).max().orElse(0);
        double avgWealth = totalWealth / people.size();
        double meanDiff = 0;
        for (Person p1 : people) {
            for (Person p2 : people) {
                meanDiff += Math.abs(p1.getWealth() - p2.getWealth());
            }
        }
        double gini = meanDiff / (2 * people.size() * people.size() * avgWealth);

        System.out.printf("Tick %d: Total Wealth = %.2f, Min = %.2f, Max = %.2f, Avg = %.2f, Gini = %.4f%n",
                tick, totalWealth, minWealth, maxWealth, avgWealth, gini);
        
    }

    public void runSimulation() {
        // Start patch threads
        for (int i = 0; i < maxCoord; i++) {
            for (int j = 0; j < maxCoord; j++) {
                map[i][j].start();
            }
        }

        // Start person threads
        for (Person person : people) {
            person.start();
        }

        // Simulation loop
        for (int tick = 0; tick < Params.MAX_TICK; tick++) {
            try {
                // Reproduction and Population Maintenance (Replace dead agents)
                processReproduction();

                // Print statistics every 100 ticks
                if (tick % Params.PRINT_INTERVAL == 0) {
                    printStatistics(tick);
                }

                Thread.sleep(Params.GRAIN_GROWTH_INTERVAL);
            } catch (InterruptedException e) {
                break;
            }
        }

        // Stop all threads
        for (Person person : people) {
            person.interrupt();
        }
        for (int i = 0; i < maxCoord; i++) {
            for (int j = 0; j < maxCoord; j++) {
                map[i][j].interrupt();
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