package person.strategy;

import person.Person;
import util.Params;
import world.Patch;
import world.World;

public class RandomSpawnStrategy implements ReproductionStrategy{
    @Override
    public synchronized void reproduce(Person person, World world) {
        Patch[][] map = world.getMap();

        if (!person.isAliveAfterMetabolize()) {
            int x, y;
            Patch patch;
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

    @Override
    public String toString() {
        return "where offspring will randomly spawn without inheritance.";
    }
}
