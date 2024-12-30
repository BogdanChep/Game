package Game;

import java.io.Serializable;
import java.util.Random;
/**
 * Класс AI представляет собой реализацию искусственного интеллекта.
 * Он реализует интерфейс Serializable.
 */
public class AI implements Serializable {
    private int вода;
    private int рис;
    private int крестьяне;
    private int дома;
    private GameMap map; // Ссылка на GameMap
    private Random random;

    public AI(GameMap map) {
        this.map = map; // Передача GameMap в конструктор
        this.вода = 10;
        this.рис = 10;
        this.крестьяне = 5;
        this.дома = 0; // Изначально домов нет
        this.random = new Random();
    }

    public void makeDecision() {
        tryCaptureTerritory(); // Теперь ИИ будет пытаться захватить территорию на каждой итерации
        gatherWater();
        buildHouse();
        producePeasants(); // Добавляем производством крестьян в конце дня
        System.out.println("ИИ принял решение.");
    }

    private void tryCaptureTerritory() {
        for (int attempt = 0; attempt < 10; attempt++) {
            int x = random.nextInt(map.size);
            int y = random.nextInt(map.size);
            int peasantsNeeded = map.getTerritory(x, y);
            if (canCaptureTerritory(peasantsNeeded)) {
                map.controlTerritoryByAI(x, y);
                крестьяне -= peasantsNeeded; // Уменьшаем количество крестьян при захвате
                System.out.println("ИИ захватил территорию на (" + x + ", " + y + ").");
                break; // Выходим из цикла после успешного захвата
            }
        }
    }

    private boolean canCaptureTerritory(int peasantsNeeded) {
        return крестьяне >= peasantsNeeded && вода >= peasantsNeeded && рис >= peasantsNeeded;
    }

    private void gatherWater() {
        вода += 5;
        System.out.println("ИИ собрал воду. Текущая вода: " + вода);
    }

    public void collectResources() {
        рис += крестьяне;
        System.out.println("ИИ собрал ресурсы. Текущий рис: " + рис);
    }

    public void growRice() {
        рис += 3;
        System.out.println("Рис ИИ растет. Текущий рис: " + рис);
    }

    public void producePeasants() {
        if (дома > 0) {
            крестьяне += дома;
            System.out.println("ИИ произвел " + дома + " новых крестьян.");
        }
    }

    public int getWater() {
        return вода;
    }

    public int getRice() {
        return рис;
    }

    public int getMobileUnits() {
        return крестьяне;
    }

    public void buildHouse() {
        if (вода >= 5 && рис >= 10) {
            вода -= 5;
            рис -= 10;
            дома++;
            System.out.println("ИИ построил дом. Количество домов: " + дома);
        } else {
            System.out.println("Недостаточно ресурсов для постройки дома.");
        }
    }
}