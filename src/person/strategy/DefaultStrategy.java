package person.strategy;

import person.Person;
import world.Patch;
import world.World;

public class DefaultStrategy implements ReproductionStrategy {

    @Override
    public synchronized void reproduce(Person person, World world) {
        Patch parentPatch = person.getCurrentPatch();

        if (!person.isAliveAfterMetabolize()) {
            Person newPerson = new Person(world);
            newPerson.setPatch(parentPatch);

            parentPatch.setPerson(newPerson);
            world.getPeople().remove(person);
            world.getPeople().add(newPerson);
        }
    }

    @Override
    public String toString() {
        return "Default";
    }
}
