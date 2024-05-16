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
    private static final int[][][] MAP = new int[NUM_TILES_X][NUM_TILES_Y][5];
    private int currentFloor = 0;
    private int playerX = 0;
    private int playerY = 0;

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
        Random random = new Random();
        for (int x = 0; x < NUM_TILES_X; x++) {
            for (int y = 0; y < NUM_TILES_Y; y++) {
                for (int f = 0; f < 5; f++) {
                    MAP[x][y][f] = random.nextInt(2); // 0 - Empty, 1 - Wall
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.clearRect(0, 0, WIDTH, HEIGHT);

        for (int x = 0; x < NUM_TILES_X; x++) {
            for (int y = 0; y < NUM_TILES_Y; y++) {
                if (MAP[x][y][currentFloor] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Draw player
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

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}
