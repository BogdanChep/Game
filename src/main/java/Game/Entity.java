package Game;

import java.io.Serializable;
import java.util.Random;

/**
 * Базовый класс Entity представляет собой реализацию общих свойств
 * и методов для игрока и ИИ.
 */
public abstract class Entity implements Serializable {
    protected int вода;
    protected int рис;
    protected int крестьяне;
    protected int дома;
    protected Random random;

    public Entity() {
        this.вода = 10;
        this.рис = 10;
        this.крестьяне = 5;
        this.дома = 0; // Изначально домов нет
        this.random = new Random();
    }

    public void gatherWater() {
        вода += крестьяне * 5;
        System.out.println("Собрана вода. Текущая вода: " + вода);
    }

    public void growRice() {
        рис += 3;
        System.out.println("Рис вырос. Текущий рис: " + рис);
    }

    public void collectResources() {
        рис += крестьяне;
        System.out.println("Собраны ресурсы. Текущий рис: " + рис);
    }

    public void producePeasants() {
        if (дома > 0) {
            крестьяне += дома;
            System.out.println(getClass().getSimpleName() + " произвел " + дома + " новых крестьян.");
        }
    }

    public int getWater() {
        return вода;
    }

    public int getRice() {
        return рис;
    }

    public int getPeasants() {
        return крестьяне;
    }

    public int getHouses() {
        return дома;
    }

    public void buildHouse() {
        if (вода >= 5 && рис >= 10) {
            вода -= 5;
            рис -= 10;
            дома++;
            System.out.println(getClass().getSimpleName() + " построил дом. Количество домов: " + дома);
        } else {
            System.out.println("Недостаточно ресурсов для постройки дома.");
        }
    }

    public void validateResources(int peasantsNeeded) {
        // Проверка на достаточность ресурсов
        if (вода < peasantsNeeded || рис < peasantsNeeded || крестьяне < peasantsNeeded) {
            throw new IllegalArgumentException("Недостаточно ресурсов для выполнения действия.");
        }
    }

    // Новый метод для получения числа мобильных единиц (крестьян)
    public int getMobileUnits() {
        return крестьяне;
    }
}
