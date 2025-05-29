package person.strategy;

import person.Person;
import world.Patch;
import world.World;

public class InheritanceStrategy implements ReproductionStrategy {
    @Override
    public synchronized void reproduce(Person person, World world) {
        int inheritance = person.getWealth();
        Patch parentPatch = person.getCurrentPatch();

        if (!person.isAliveAfterMetabolize()) {
            Person newPerson = new Person(world);
            newPerson.setPatch(parentPatch);
            newPerson.setWealth(inheritance);

            parentPatch.setPerson(newPerson);
            world.getPeople().remove(person);
            world.getPeople().add(newPerson);
        }
    }

    @Override
    public String toString() {
        return "where offspring will spawn at its parent's patch with inheritance of wealth.";
    }
}
