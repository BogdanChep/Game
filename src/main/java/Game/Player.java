package Game;

import java.io.Serializable;

/**
 * Класс Player представляет собой реализацию класса, отвечающего за поведение игрока.
 */
public class Player extends Entity {
    public Player() {
        super(); // Вызов конструктора родительского класса
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
        validateResources(peasantsNeeded);

        if (!map.isTerritoryControlledByPlayer(x, y) && !map.isTerritoryControlledByAI(x, y)) {
            map.controlTerritoryByPlayer(x, y);
            крестьяне -= peasantsNeeded;
            вода -= peasantsNeeded;
            рис -= peasantsNeeded;
            System.out.println("Расширили территорию на (" + x + ", " + y + ").");
        } else {
            System.out.println("Эта территория уже занята.");
        }
    }
}
