/* This class is in charge of how people move within the system */

package src;
import java.util.Random;

public class Person extends Thread {
    private int lifeExpectancy;
    private int age; 
    private int metabolism;
    private int wealth;
    private int vision;

    // Creation of a person will generate values to make them have unique attributes
    // Welath isn't inherited in this system.
    public Person() {
        this.lifeExpectancy = Params.lifeExpectancy();
        Random random = new Random();
        this.age = random.nextInt(this.lifeExpectancy); // even if a person is created, their actual age is still randomise
        this.metabolism = Params.metabolism();
        this.wealth = random.nextInt(50) + this.metabolism;
        this.vision = Params.vision();
    }
    
}
