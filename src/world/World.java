/* This is the class that simulates the model.
 * It mainly controls the patches and their coordinates.
 * People are also created here.
 */

package world;

import java.util.ArrayList;
import java.util.List;
import person.Person;
import person.strategy.ReproductionStrategy;
import util.Params;

public class World {
    public static final int maxCoord = 51; //not a parameter; determines max size of map
    private Patch[][] map; 
    private List<Person> people;
    private ReproductionStrategy strategy;
    private int numPeople;
    private int grainGrowthRate;


    /**
     * Constructor to initialize the world with a specified reproduction strategy.
     * Sets up the patches and people on the map.
     *
     * @param numPeople the number of people to place on the map
     * @param grainGrowthRate the interval at which grain grows on patches
     * @param strategy the reproduction strategy to use for the simulation
     */
    public World(int numPeople, int grainGrowthRate, ReproductionStrategy strategy) {
        this.people = new ArrayList<>();
        this.strategy = strategy;
        this.numPeople = numPeople;
        this.grainGrowthRate = grainGrowthRate;
        setupPatches();
        setupPeople();

    }

    //--- Setup functions ---//

    /**
     * Initializes people by placing them randomly on patches of the map.
     * Ensures no two people occupy the same patch at setup.
     */
    public void setupPeople() {
        people.clear();
        for (int i = 0; i < this.numPeople; i++) {
            int x, y;
            Patch patch;

            // This guarantees that the new person will be added
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

    /**
     * Initializes the map with patches of varying grain productivity.
     * Performs multiple diffusion steps to spread grain and then sets
     * the maximum grain levels for each patch.
     */
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

    /**
     * Prints statistical information about the wealth distribution
     * of all people in the simulation at the specified tick.
     * Calculates total, minimum, maximum, and average wealth, as well as Gini coefficient.
     *
     * @param tick the current simulation tick at which statistics are printed
     */
    protected void printStatistics(int tick) {
        double totalWealth = people.stream().mapToDouble(Person::getWealth).sum();
        double maxWealth = people.stream().mapToDouble(Person::getWealth).max().orElse(0);
        double avgWealth = totalWealth / people.size();

        double meanDiff = 0;
        int numLowerClass = 0;
        int numMiddleClass = 0;
        int numUpperClass = 0;
        double oneThird = (1.0 / 3) * maxWealth;
        double twoThirds = (2.0 / 3) * maxWealth;


        for (Person p1 : people) {
            double wealth = p1.getWealth();

            if (wealth <= oneThird) {
                numLowerClass++;
            } else if (wealth <= twoThirds) {
                numMiddleClass++;
            } else {
                numUpperClass++;
            }

            for (Person p2 : people) {
                meanDiff += Math.abs(p1.getWealth() - p2.getWealth());
            }
        }
        double gini = meanDiff / (2 * people.size() * people.size() * avgWealth);

        System.out.printf("Tick %d: Total Wealth = %.2f, Gini = %.4f, Low = %d, Middle = %d, Upper = %d%n",
                tick, totalWealth, gini, numLowerClass, numMiddleClass, numUpperClass);
        
    }

    /**
     * Runs the simulation loop for the world.
     * On each tick, replenishes grain on patches at intervals,
     * lets each person act according to the reproduction strategy,
     * and periodically prints statistics.
     */
    public void runSimulation() {
        System.out.println("Starting Wealth Distribution Simulation, " + strategy);
        // Simulation loop
        for (int tick = 0; tick < Params.MAX_TICK+1; tick++) {

            // Replenish
            if (tick % this.grainGrowthRate == 0) {
                for (Patch[] row : map) {
                    for (Patch patch : row) {
                        // Replenish
                        patch.grow();
                    }
                }
            }

            // Make copy to avoid concurrent modification
            List<Person> tmpPeople = new ArrayList<>(this.people);
            for (Person agent : tmpPeople) {
                agent.tick(strategy);
            }

            // Print statistics every 100 ticks
            if (tick % Params.PRINT_WRITE_INTERVAL == 0) {
                printStatistics(tick);
            }
            tick++;

        }
    }

    //--- Getter and setter functions ---//

    public Patch[][] getMap() {
        return map;
    }

    public List<Person> getPeople() {
        return this.people;
    }
}