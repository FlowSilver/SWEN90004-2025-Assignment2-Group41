package person.strategy;

import person.Person;
import world.World;

public interface ReproductionStrategy {
    void reproduce(Person person, World world);  // method to be implemented by classes
}