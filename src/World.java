package src;
import java.io.*;
import java.util.*;

public class World {
    public static void main (String[] args) {
        for (int i = 0; i<Params.TOTAL_PEOPLE+1; i++) {
            Person person = new Person();
            System.out.println(person.lifeTotal());
        }
    }
}