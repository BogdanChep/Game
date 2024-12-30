package Game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JFrame {
    private static final Logger logger = LogManager.getLogger(Game.class);
    Player player;
    AI ai;
    GameMap map;
    JLabel statusLabel;
    private JLabel aiStatusLabel;
    private JPanel gamePanel;
    private Random random;
    int dayCount = 0;

    // Списки для хранения значений ресурсов по дням
    private List<Integer> playerWaterHistory = new ArrayList<>();
    private List<Integer> playerRiceHistory = new ArrayList<>();
    private List<Integer> playerPeasantsHistory = new ArrayList<>();
    private List<Integer> aiWaterHistory = new ArrayList<>();
    private List<Integer> aiRiceHistory = new ArrayList<>();
    private List<Integer> aiPeasantsHistory = new ArrayList<>();

    public Game() {
        this.player = new Player();
        this.map = new GameMap();
        this.ai = new AI(map);
        this.random = new Random();

        this.setTitle("Экспансия");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeUI();
    }

    private void initializeUI() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(new Color(200, 200, 200));

        statusLabel = new JLabel("Статус игрока: ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        updateStatus();

        aiStatusLabel = new JLabel("Статус ИИ: ");
        aiStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        updateAIStatus();

        // Кнопки управления
        JButton gatherWaterButton = createButton("Собрать воду", e -> {
            player.gatherWater();
            updateStatus();
            checkActions(); // Проверка действий после хода игрока
        });

        JButton plantRiceButton = createButton("Полить и собрать рис", e -> {
            if (canPerformAction()) {
                player.growRice();
                updateStatus();
                checkActions();
            }
        });

        JButton expandButton = createButton("Захватить территорию", e -> {
            if (canPerformAction()) {
                int x = random.nextInt(map.size);
                int y = random.nextInt(map.size);
                int neededPeasants = map.getTerritory(x, y);
                if (neededPeasants <= player.getPeasants() && player.getWater() >= neededPeasants && player.getRice() >= neededPeasants) {
                    player.collectResources(); // Сбор ресурсов игроком перед захватом
                    map.controlTerritoryByPlayer(x, y);
                    updateStatus();
                    updateAIStatus();
                    checkActions();
                } else {
                    showErrorMessage("Недостаточно ресурсов для захвата территории.");
                }
            }
        });

        JButton buildButton = createButton("Построить дом", e -> {
            if (canPerformAction()) {
                player.buildHouse();
                updateStatus();
                checkActions();
            }
        });

        JButton saveButton = createButton("Сохранить игру", e -> saveGame());
        JButton loadButton = createButton("Загрузить игру", e -> loadGame());
        JButton exitButton = createButton("Выйти из игры", e -> exitGame());
        JButton newGameButton = createButton("Начать новую игру", e -> startNewGame());

        // Добавление кнопок в панель управления
        controlPanel.add(gatherWaterButton);
        controlPanel.add(plantRiceButton);
        controlPanel.add(expandButton);
        controlPanel.add(buildButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(exitButton);
        controlPanel.add(newGameButton);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(2, 1));
        statusPanel.add(statusLabel);
        statusPanel.add(aiStatusLabel);

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(400, 400));

        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(statusPanel, BorderLayout.CENTER);
        this.add(gamePanel, BorderLayout.SOUTH);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.addActionListener(action);
        return button;
    }

    private void exitGame() {
        logger.info("Выход из игры");
        System.exit(0);
    }

    void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
        logger.warn(message);
    }

    void executeEndOfDay() {
        ai.makeDecision(); // Ход ИИ сразу после действий игрока
        player.collectResources(); // Сбор ресурсов игроком
        player.growRice(); // Увеличение риса игроком
        ai.collectResources(); // Сбор ресурсов ИИ
        ai.growRice(); // Увеличение риса ИИ

        player.producePeasants(); // Производство крестьян игроком
        ai.producePeasants(); // Производство крестьян ИИ

        // Проверка на недостаток воды у игрока
        if (player.getWater() < 0) {
            showErrorMessage("Население умирает из-за нехватки воды! Будьте осторожны!");
            disableActions(); // Отключаем действия, кроме сбора воды
        } else {
            enableActions(); // Включаем действия, если воды достаточно
        }

        // Сохраняем текущие значения ресурсов
        playerWaterHistory.add(player.getWater());
        playerRiceHistory.add(player.getRice());
        playerPeasantsHistory.add(player.getPeasants());
        aiWaterHistory.add(ai.getWater());
        aiRiceHistory.add(ai.getRice());
        aiPeasantsHistory.add(ai.getMobileUnits());

        dayCount++;

        logger.info("Конец дня " + dayCount + ": " + updateGameState());
        checkGameOver();
        gamePanel.repaint();
    }

    private String updateGameState() {
        return "Статус игрока - Вода: " + player.getWater() + ", Рис: " + player.getRice() +
                ", Крестьяне: " + player.getPeasants() + "; " +
                "Статус ИИ - Вода: " + ai.getWater() + ", Рис: " + ai.getRice() +
                ", Крестьяне: " + ai.getMobileUnits();
    }

    void updateStatus() {
        statusLabel.setText("Вода: " + player.getWater() + ", Рис: " + player.getRice() +
                ", Крестьяне: " + player.getPeasants() +
                ", Захваченная территория: " + map.getControlledArea(player) + "%");
    }

    private void updateAIStatus() {
        aiStatusLabel.setText("Вода ИИ: " + ai.getWater() + ", Рис ИИ: " + ai.getRice() +
                ", Крестьяне ИИ: " + ai.getMobileUnits() +
                ", Захваченная территория ИИ: " + map.getControlledArea(ai) + "%");
    }

    private void checkGameOver() {
        if (map.getControlledArea(player) >= 50) {
            declareWinner("Игрок");
        } else if (map.getControlledArea(ai) >= 50) {
            declareWinner("ИИ");
        }
    }

    private void declareWinner(String winner) {
        JOptionPane.showMessageDialog(this, winner + " победил!", "Конец игры", JOptionPane.INFORMATION_MESSAGE);
        logger.info(winner + " победил!");
    }

    private void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game_save.dat"))) {
            oos.writeObject(player);
            oos.writeObject(ai);
            oos.writeObject(map);
            JOptionPane.showMessageDialog(this, "Игра успешно сохранена!", "Сохранение игры", JOptionPane.INFORMATION_MESSAGE);
            logger.info("Игра успешно сохранена.");
        } catch (IOException e) {
            logger.error("Ошибка сохранения игры.", e);
            showErrorMessage("Ошибка сохранения игры.");
        }
    }

    private void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game_save.dat"))) {
            player = (Player) ois.readObject();
            ai = (AI) ois.readObject();
            map = (GameMap) ois.readObject();
            updateStatus();
            updateAIStatus();
            gamePanel.repaint();
            JOptionPane.showMessageDialog(this, "Игра успешно загружена!", "Загрузка игры", JOptionPane.INFORMATION_MESSAGE);
            logger.info("Игра успешно загружена.");
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка загрузки игры.", e);
            showErrorMessage("Ошибка загрузки игры.");
        }
    }

    private void startNewGame() {
        // Инициализация нового игрока, ИИ и карты
        player = new Player(); // Создаем нового игрока
        map = new GameMap(); // Создаем новую карту
        ai = new AI(map); // Создаем нового ИИ с новой картой
        dayCount = 0; // Сбрасываем счетчик дней

        // Очищаем списки истории ресурсов
        playerWaterHistory.clear();
        playerRiceHistory.clear();
        playerPeasantsHistory.clear();
        aiWaterHistory.clear();
        aiRiceHistory.clear();
        aiPeasantsHistory.clear();

        // Обновляем статус игрока и ИИ
        updateStatus();
        updateAIStatus();
        gamePanel.repaint(); // Перерисовываем панель игры

        logger.info("Начата новая игра.");
    }

    boolean canPerformAction() {
        if (player.getWater() < 1) {
            showErrorMessage("Население умирает из-за нехватки воды! Другие действия недоступны, кроме сбора воды.");
            return false;
        }
        return true;
    }

    private void enableActions() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(true);
            }
        }
    }

    private void disableActions() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (!button.getText().equals("Собрать воду")) {
                    button.setEnabled(false);
                }
            }
        }
    }

    private void checkActions() {
        executeEndOfDay(); // Вызываем конец дня после действий игрока
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(new BufferedOutputStream(System.out), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new BufferedOutputStream(System.err), true, StandardCharsets.UTF_8));

        SwingUtilities.invokeLater(Game::new);
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMap(g);
        }

        private void drawMap(Graphics g) {
            int cellSize = 40;
            for (int i = 0; i < map.size; i++) {
                for (int j = 0; j < map.size; j++) {
                    int x = j * cellSize;
                    int y = i * cellSize;

                    g.setColor(new Color(180, 180, 180)); // Светло-серый для поля
                    if (map.isTerritoryControlledByPlayer(i, j)) {
                        g.setColor(new Color(68, 85, 90)); // Темно-серый для территории игрока
                    } else if (map.isTerritoryControlledByAI(i, j)) {
                        g.setColor(new Color(154, 85, 85)); // Очень темно-серый для территории ИИ
                    }
                    g.fillRect(x, y, cellSize, cellSize);

                    int peasantsNeeded = map.getTerritory(i, j);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellSize, cellSize);
                    g.drawString(peasantsNeeded + "", x + cellSize / 2 - 10, y + cellSize / 2 + 10);
                }
            }
        }

        public GamePanel() {
            setBackground(new Color(150, 150, 150)); // Цвет фона для панели карты
        }
    }
}
