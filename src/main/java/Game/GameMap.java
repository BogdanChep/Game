package Game;

import java.io.Serializable;
/**
 * Класс GameMap представляет собой реализацию карты игры в виде двумерного массива.
 */
public class GameMap implements Serializable {
    public final int size;
    private int[][] territories;
    private boolean[][] controlledByPlayer;
    private boolean[][] controlledByAI;

    public GameMap() {
        this.size = 10;
        this.territories = new int[size][size];
        this.controlledByPlayer = new boolean[size][size];
        this.controlledByAI = new boolean[size][size];
        initializeTerritories();
    }

    private void initializeTerritories() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                territories[i][j] = (int) (Math.random() * 5) + 1; // Задаем количество крестьян
            }
        }
    }

    public int getControlledArea(Player player) {
        int controlled = 0;
        for (boolean[] row : controlledByPlayer) {
            for (boolean cell : row) {
                if (cell) controlled++;
            }
        }
        return (controlled * 100) / (size * size);
    }

    public int getControlledArea(AI ai) {
        int controlled = 0;
        for (boolean[] row : controlledByAI) {
            for (boolean cell : row) {
                if (cell) controlled++;
            }
        }
        return (controlled * 100) / (size * size);
    }

    public int getTerritory(int x, int y) {
        return territories[x][y];
    }

    public boolean isTerritoryControlledByPlayer(int x, int y) {
        return controlledByPlayer[x][y];
    }

    public boolean isTerritoryControlledByAI(int x, int y) {
        return controlledByAI[x][y];
    }

    public void controlTerritoryByPlayer(int x, int y) {
        controlledByPlayer[x][y] = true;
    }

    public void controlTerritoryByAI(int x, int y) {
        controlledByAI[x][y] = true;
    }
}
