/* File: Sides.java
 * Authors: Rafikov Rinat
 * Class, that contains enumeration of figure sides/directions.
 */

import java.util.*;

public enum Sides {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3),
    CURRENT(4);

    private static final List<Sides> VALUES = List.of(values());
    private static final Random RANDOM = new Random();

    private int value;

    Sides(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Sides randomLetter(){
        List<Sides> valuesCopy = new ArrayList<>(VALUES);
        valuesCopy.remove(CURRENT);
        return valuesCopy.get(RANDOM.nextInt(valuesCopy.size()));
    }
}
