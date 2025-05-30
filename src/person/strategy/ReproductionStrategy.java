package person.strategy;

import person.Person;
import world.World;

/**
 * The ReproductionStrategy interface defines a contract for implementing
 * different reproduction behaviors for Person objects in a World simulation.
 *
 * Classes that implement this interface must provide logic for how a person
 * reproduces. This may involve creating new Person instances, remove itself
 * and adding them to the world.
 *
 */
public interface ReproductionStrategy {
    /**
     * Executes the reproduction behavior for the specified person in the given world.
     *
     * This method should define how the person reproduces, including any logic
     * for creating offspring or altering the world's state to reflect reproduction.
     *
     * @param person the person attempting to reproduce
     * @param world the simulation world where reproduction takes place
     */
    void reproduce(Person person, World world);  // method to be implemented by classes
}