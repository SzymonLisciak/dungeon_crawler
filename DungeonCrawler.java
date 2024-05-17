import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class DungeonCrawler extends JFrame implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 50;
    private static final int NUM_TILES_X = WIDTH / TILE_SIZE;
    private static final int NUM_TILES_Y = HEIGHT / TILE_SIZE;
    private static final int NUM_FLOORS = 5;
    private static final int[][][] MAP = new int[NUM_TILES_X][NUM_TILES_Y][NUM_FLOORS];
    private int currentFloor = 0;
    private int playerX;
    private int playerY;
    private int exitX;
    private int exitY;

    public DungeonCrawler() {
        setTitle("Dungeon Crawler");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        generateMap();
    }

    private void generateMap() {
        // Inicjalizacja mapy jako pusta
        for (int x = 0; x < NUM_TILES_X; x++) {
            for (int y = 0; y < NUM_TILES_Y; y++) {
                for (int f = 0; f < NUM_FLOORS; f++) {
                    MAP[x][y][f] = 1; // Wypełnienie mapy ścianami
                }
            }
        }

        // Generowanie poziomów labiryntu
        Random random = new Random();
        for (int f = 0; f < NUM_FLOORS; f++) {
            dfs(random.nextInt(NUM_TILES_X), random.nextInt(NUM_TILES_Y), f);
        }

        // Ustawienie początkowej pozycji gracza wewnątrz labiryntu
        do {
            playerX = random.nextInt(NUM_TILES_X);
            playerY = random.nextInt(NUM_TILES_Y);
        } while (MAP[playerX][playerY][currentFloor] == 1); // Sprawdzenie, czy początkowa pozycja gracza nie jest ścianą

        int exitCorner = random.nextInt(4);
        switch (exitCorner) {
            case 0: // Lewy górny róg
                exitX = 0;
                exitY = 0;
                break;
            case 1: // Prawy górny róg
                exitX = NUM_TILES_X - 1;
                exitY = 0;
                break;
            case 2: // Lewy dolny róg
                exitX = 0;
                exitY = NUM_TILES_Y - 1;
                break;
            case 3: // Prawy dolny róg
                exitX = NUM_TILES_X - 1;
                exitY = NUM_TILES_Y - 1;
                break;
        }
    }

    private void dfs(int x, int y, int floor) {
        MAP[x][y][floor] = 0; // Oznaczenie bieżącego punktu jako drogi

        // Przetasowanie sąsiadów
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(4);
            int[] direction = directions[index];
            int newX = x + direction[0] * 2;
            int newY = y + direction[1] * 2;

            if (newX > 0 && newX < NUM_TILES_X - 1 && newY > 0 && newY < NUM_TILES_Y - 1 && MAP[newX][newY][floor] == 1) {
                MAP[x + direction[0]][y + direction[1]][floor] = 0; // Usunięcie ściany
                dfs(newX, newY, floor); // Rekurencyjne wywołanie DFS
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Rysowanie bieżącego poziomu
        for (int x = 0; x < NUM_TILES_X; x++) {
            for (int y = 0; y < NUM_TILES_Y; y++) {
                if (MAP[x][y][currentFloor] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Rysowanie gracza
        g.setColor(Color.BLUE);
        g.fillRect(playerX * TILE_SIZE, playerY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DungeonCrawler game = new DungeonCrawler();
            game.setVisible(true);
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP && playerY > 0 && MAP[playerX][playerY - 1][currentFloor] != 1) {
            playerY--;
        } else if (keyCode == KeyEvent.VK_DOWN && playerY < NUM_TILES_Y - 1 && MAP[playerX][playerY + 1][currentFloor] != 1) {
            playerY++;
        } else if (keyCode == KeyEvent.VK_LEFT && playerX > 0 && MAP[playerX - 1][playerY][currentFloor] != 1) {
            playerX--;
        } else if (keyCode == KeyEvent.VK_RIGHT && playerX < NUM_TILES_X - 1 && MAP[playerX + 1][playerY][currentFloor] != 1) {
            playerX++;
        }

        // Sprawdzenie, czy gracz dotarł do końca labiryntu na danym poziomie
        if (playerX == NUM_TILES_X - 1 && playerY == NUM_TILES_Y - 1) {
            if (currentFloor < NUM_FLOORS - 1) {
                currentFloor++; // Przejście na następny poziom
                generateMap(); // Wygenerowanie nowej mapy
            } else {
                // Jeśli gracz dotarł do końca ostatniego poziomu, wyświetl komunikat o ukończeniu gry
                JOptionPane.showMessageDialog(this, "Gratulacje! Ukończyłeś grę!");
                System.exit(0); // Zamknięcie gry
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
