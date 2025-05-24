package src;
import java.util.Random;
public class Params {
    public static final int MIN_LIFE = 100;
    public static final int MAX_LIFE = 1000;
    public static final int TOTAL_PEOPLE = 100;
    public static int lifeExpectancy() {
        Random random = new Random();
        return (random.nextInt(MAX_LIFE-MIN_LIFE+1)+MIN_LIFE);
    }
}