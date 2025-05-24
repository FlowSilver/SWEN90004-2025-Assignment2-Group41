package src;


public class Person {
    private int lifeNum;
    public Person() {
        this.lifeNum = Params.lifeExpectancy();
    }

    public int lifeTotal() {
        return lifeNum;
    }
}
