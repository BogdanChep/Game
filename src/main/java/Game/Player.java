package Game;

import java.io.Serializable;
/**
 * Класс Player представляет собой реализацию класса, отвечающего за поведение игрока
 * В данном классе также реализуются функции игрока, крестьян, ИИ, строений и территории.
 */
public class Player implements Serializable {
    private int вода;
    private int рис;
    private int крестьяне;
    private int дома;

    public Player() {
        this.вода = 10;
        this.рис = 10;
        this.крестьяне = 5;
        this.дома = 0; // Изначально домов нет
    }

    public void gatherWater() {
        вода += крестьяне * 5;
        System.out.println("Собрана вода. Текущая вода: " + вода);
    }

    public void waterRice() {
        if (вода > 0) {
            рис += 3;
            вода--;
            System.out.println("Поливаем рис. Текущий рис: " + рис);
        } else {
            System.out.println("Недостаточно воды для полива риса.");
        }
    }

    public void expandTerritory(GameMap map, int x, int y) {
        int peasantsNeeded = map.getTerritory(x, y);
        if (peasantsNeeded <= крестьяне && вода >= peasantsNeeded && рис >= peasantsNeeded) {
            if (!map.isTerritoryControlledByPlayer(x, y) && !map.isTerritoryControlledByAI(x, y)) {
                map.controlTerritoryByPlayer(x, y);
                крестьяне -= peasantsNeeded;
                вода -= peasantsNeeded;
                рис -= peasantsNeeded;
                System.out.println("Расширили территорию на (" + x + ", " + y + ").");
            } else {
                System.out.println("Эта территория уже занята.");
            }
        } else {
            System.out.println("Недостаточно ресурсов для захвата территории.");
        }
    }

    public void buildHouse(GameMap map) {
        if (вода >= 5 && рис >= 10) {
            вода -= 5;
            рис -= 10;
            дома++;
            System.out.println("Построен дом. Количество домов: " + дома);
        } else {
            System.out.println("Недостаточно ресурсов для постройки дома.");
        }
    }

    public void collectResources() {
        рис += крестьяне;
        System.out.println("Собраны ресурсы. Текущий рис: " + рис);
    }

    public void growRice() {
        рис += 3;
        System.out.println("Рис вырос. Текущий рис: " + рис);
    }

    public void producePeasants() {
        final int REQUIRED_RICE = 2;
        final int REQUIRED_WATER = 1;

        if (дома > 0) {
            int newPeasants = Math.min(дома, (вода / REQUIRED_WATER) + (рис / REQUIRED_RICE));
            крестьяне += newPeasants;
            вода -= newPeasants * REQUIRED_WATER;
            рис -= newPeasants * REQUIRED_RICE;
            System.out.println("Игрок произвел " + newPeasants + " новых крестьян.");
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
}
