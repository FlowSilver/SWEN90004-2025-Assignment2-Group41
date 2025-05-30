package world;

import person.Person;
import person.strategy.ReproductionStrategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * WorldWriter extends the World class to add functionality for writing
 * simulation statistics to a CSV file. It maintains a BufferedWriter to
 * output wealth distribution statistics for each simulation tick.
 */
public class WorldWriter extends World {
    private static String outputPath = "stat";
    private BufferedWriter writer;
    private String fileName;
    private Path filePath;

    /**
     * Constructs a WorldWriter instance.
     * Initializes the world simulation and prepares the CSV file for output.
     *
     * @param numPeople number of people in the simulation
     * @param grainGrowthRate rate at which grain replenishes
     * @param strategy reproduction strategy used by the people
     * @param fileName name of the output CSV file to write statistics
     */
    public WorldWriter(int numPeople, int grainGrowthRate, ReproductionStrategy strategy, String fileName) {
        super(numPeople, grainGrowthRate, strategy);
        this.fileName = fileName;

        createPath();
        // Initialize CSV file writer
        try {
            writer = new BufferedWriter(new FileWriter(filePath.toFile(), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the directory path and resolves the output CSV file path.
     * If the directory does not exist, it will be created.
     */
    private void createPath() {

        try {
            // Create the directory if it doesn't exist
            Path dirPath = Paths.get(outputPath);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Create the CSV file path
            this.filePath = dirPath.resolve(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the current statistics of the world simulation to the CSV file.
     * Statistics include tick, total wealth, minimum, maximum, average wealth,
     * and the Gini coefficient measuring inequality.
     *
     * @param world the World instance from which to retrieve statistics
     * @param tick the current simulation tick number
     */
    private void writeStatistics(World world, int tick) {
        List<Person> people = world.getPeople();
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

        // Write to CSV file in the exact format
        try {
            String stats = String.format("%d, %.2f, %.2f, %.2f, %.2f, %.4f",
                    tick, totalWealth, minWealth, maxWealth, avgWealth, gini);
            writer.write(stats + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints statistics to the console and writes them to the CSV file.
     * This method overrides the parent class's printStatistics to add file output.
     *
     * @param tick the current simulation tick number
     */
    public void printStatistics(int tick) {
        super.printStatistics(tick);
        writeStatistics(this, tick);
    }


}
