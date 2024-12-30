package Game;

import java.io.Serializable;

/**
 * Класс AI представляет собой реализацию искусственного интеллекта.
 * Он реализует интерфейс Serializable.
 */
public class AI extends Entity {
    private GameMap map;

    public AI(GameMap map) {
        super();
        this.map = map;
    }

    public void makeDecision() {
        tryCaptureTerritory();
        gatherWater();
        buildHouse();
        producePeasants();
        System.out.println("ИИ принял решение.");
    }

    private void tryCaptureTerritory() {
        for (int attempt = 0; attempt < 10; attempt++) {
            int x = random.nextInt(map.size);
            int y = random.nextInt(map.size);
            int peasantsNeeded = map.getTerritory(x, y);
            if (canCaptureTerritory(peasantsNeeded)) {
                map.controlTerritoryByAI(x, y);
                крестьяне -= peasantsNeeded;
                System.out.println("ИИ захватил территорию на (" + x + ", " + y + ").");
                break;
            }
        }
    }

    private boolean canCaptureTerritory(int peasantsNeeded) {
        return крестьяне >= peasantsNeeded && вода >= peasantsNeeded && рис >= peasantsNeeded;
    }

    public int getPeasants() { // Новый метод, чтобы избежать ошибки
        return крестьяне;
    }
}