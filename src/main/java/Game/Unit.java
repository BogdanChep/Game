package Game;

import java.io.Serializable;
/**
 * Класс Unit представляет собой реализацию класса, отвечающего за крестьян..
 */
public class Unit implements Serializable {
    private String type;

    public Unit(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
