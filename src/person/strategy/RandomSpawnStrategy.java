package person.strategy;

import person.Person;
import util.Params;
import world.Patch;
import world.World;


/**
 * The RandomSpawnStrategy class implements the ReproductionStrategy interface.
 *
 * This strategy replaces a dead person with a new person randomly spawned in an
 * unoccupied patch of the world map. It ensures that the new person is only placed
 * in a patch that does not already contain another person.
 *
 */
public class RandomSpawnStrategy implements ReproductionStrategy{

    /**
     * Replaces a dead person with a new person spawned at a random unoccupied patch.
     *
     * If the given person has died after metabolizing, a new Person object is created
     * and placed in a randomly selected empty patch. The dead person is removed from
     * the world's people list and the new person is added in their place.
     *
     * @param person the person to check for reproduction (only replaced if dead)
     * @param world the simulation world in which the reproduction takes place
     */
    @Override
    public void reproduce(Person person, World world) {
        Patch[][] map = world.getMap();

        if (!person.isAliveAfterMetabolize()) {
            int x, y;
            Patch patch;

            // New person is placed in a randomly selected empty patch
            do {
                x = Params.rollCoord();
                y = Params.rollCoord();
                patch = map[x][y];
            } while (patch.getPerson() != null);
            Person newPerson = new Person(world);
            newPerson.setPatch(patch);
            patch.setPerson(newPerson);

            world.getPeople().remove(person);
            person.getCurrentPatch().setPerson(null);
            world.getPeople().add(newPerson);
        }
    }

    /**
     * Returns the name of this reproduction strategy.
     *
     * @return the string "RandomSpawn"
     */
    @Override
    public String toString() {
        return "RandomSpawn";
    }
}
