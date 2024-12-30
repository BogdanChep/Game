package Game;

import java.io.Serializable;
/**
 * Класс Building представляет собой реализацию функции постройки зданий для крестьян внутри игры.
 * Он реализует интерфейс Serializable.
 */
public class Building implements Serializable {
    private String name;
    private int waterCost;
    private int riceCost;

    public Building(String name, int waterCost, int riceCost) {
        this.name = name;
        this.waterCost = waterCost;
        this.riceCost = riceCost;
    }

    public int getWaterCost() {
        return waterCost;
    }

    public int getRiceCost() {
        return riceCost;
    }
}
