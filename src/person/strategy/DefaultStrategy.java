package person.strategy;

import person.Person;
import world.Patch;
import world.World;

/**
 * The DefaultStrategy class implements the ReproductionStrategy interface.
 *
 * This strategy handles reproduction by simply replacing a dead person
 * with a new one in the same patch, without inheriting any wealth or
 * random relocation. It represents the most basic form of reproduction in the NetLogo model.
 */
public class DefaultStrategy implements ReproductionStrategy {


    /**
     * Replaces a dead person with a new person in the same patch.
     *
     * If the specified person dies after metabolizing, this method creates
     * a new Person object, places them in the same patch as the deceased
     * person, removes the dead person from the world, and adds the new
     * person to the population.
     *
     * @param person the person to check for reproduction (only replaced if dead)
     * @param world the simulation world where reproduction occurs
     */
    @Override
    public void reproduce(Person person, World world) {
        Patch parentPatch = person.getCurrentPatch();

        if (!person.isAliveAfterMetabolizeAndAging()) {
            Person newPerson = new Person(world);
            newPerson.setPatch(parentPatch);

            parentPatch.setPerson(newPerson);
            world.getPeople().remove(person);
            world.getPeople().add(newPerson);
        }
    }

    /**
     * Returns the name of this reproduction strategy.
     *
     * @return the string "Default"
     */
    @Override
    public String toString() {
        return "Default";
    }
}
