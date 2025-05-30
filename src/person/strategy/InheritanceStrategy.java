package person.strategy;

import person.Person;
import world.Patch;
import world.World;

/**
 * The InheritanceStrategy class implements the ReproductionStrategy interface.
 *
 * This strategy models reproduction through inheritance. When a person dies
 * after metabolizing, a new person is created in the same patch, inheriting
 * the exact amount of wealth from the dead person.
 *
 * The new person replaces the dead one on the same patch in the world.
 */
public class InheritanceStrategy implements ReproductionStrategy {

    /**
     * Replaces a dead person with a new person who inherits their wealth.
     *
     * If the given person has died after metabolizing, a new Person is created
     * at the same patch, inheriting the deceased person's wealth. The old person
     * is removed from the world's list of people and the new one is added.
     *
     * @param person the person to check for reproduction (only replaced if dead)
     * @param world the simulation world where the reproduction takes place
     */
    @Override
    public void reproduce(Person person, World world) {
        int inheritance = person.getWealth();
        Patch parentPatch = person.getCurrentPatch();

        // The new Person is created at the same patch
        if (!person.isAliveAfterMetabolize()) {
            Person newPerson = new Person(world);
            newPerson.setPatch(parentPatch);
            newPerson.setWealth(inheritance);

            parentPatch.setPerson(newPerson);
            world.getPeople().remove(person);
            world.getPeople().add(newPerson);
        }
    }

    /**
     * Returns the name of this reproduction strategy.
     *
     * @return the string "Inheritance"
     */
    @Override
    public String toString() {
        return "Inheritance";
    }
}
