import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int UNIT_SIZE = 25; // Size of each unit (snake body or food)
    private final int GAME_WIDTH = 600; // Width of the game area
    private final int GAME_HEIGHT = 600; // Height of the game area
    private final int DELAY = 100; // Delay between movements (game speed)

    private final ArrayList<Point> snake = new ArrayList<>(); // List to store snake's body parts
    private Point food; // Position of the food
    private Direction direction = Direction.RIGHT; // Initial direction of the snake
    private boolean running = false; // Indicates if the game is running
    private Timer timer; // Timer for game loop
    private Random random = new Random();

    public SnakeGame() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        startGame();
    }

    private void startGame() {
        newGame();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void newGame() {
        snake.clear();
        snake.add(new Point(GAME_WIDTH / 2, GAME_HEIGHT / 2)); // Start snake at the center
        spawnFood();
        direction = Direction.RIGHT;
        running = true;
    }

    private void spawnFood() {
        int x = random.nextInt((GAME_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = random.nextInt((GAME_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);

        // Ensure food does not spawn inside the snake
        for (Point part : snake) {
            if (part.equals(food)) {
                spawnFood();
                return;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Head color
                } else {
                    g.setColor(new Color(45, 180, 0)); // Body color
                }
                Point part = snake.get(i);
                g.fillRect(part.x, part.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Display score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + (snake.size() - 1), (GAME_WIDTH - metrics.stringWidth("Score: " + (snake.size() - 1))) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (GAME_WIDTH - metrics.stringWidth("Game Over")) / 2, GAME_HEIGHT / 2);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + (snake.size() - 1), (GAME_WIDTH - metrics.stringWidth("Score: " + (snake.size() - 1))) / 2, (GAME_HEIGHT / 2) + 75);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Press R to Replay", (GAME_WIDTH - metrics.stringWidth("Press R to Replay")) / 2, (GAME_HEIGHT / 2) + 150);

        timer.stop();
    }

    private void move() {
        Point head = snake.get(0);

        switch (direction) {
            case UP -> head = new Point(head.x, head.y - UNIT_SIZE);
            case DOWN -> head = new Point(head.x, head.y + UNIT_SIZE);
            case LEFT -> head = new Point(head.x - UNIT_SIZE, head.y);
            case RIGHT -> head = new Point(head.x + UNIT_SIZE, head.y);
        }

        snake.add(0, head); // Add new head position
        if (!head.equals(food)) {
            snake.remove(snake.size() - 1); // Remove tail if no food eaten
        } else {
            spawnFood(); // Spawn new food
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        // Check collision with walls
        if (head.x < 0 || head.x >= GAME_WIDTH || head.y < 0 || head.y >= GAME_HEIGHT) {
            running = false;
        }

        // Check collision with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP && direction != Direction.DOWN) {
            direction = Direction.UP;
        } else if (key == KeyEvent.VK_DOWN && direction != Direction.UP) {
            direction = Direction.DOWN;
        } else if (key == KeyEvent.VK_LEFT && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        } else if (key == KeyEvent.VK_RIGHT && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        } else if (key == KeyEvent.VK_R && !running) { // Replay on 'R' key press
            newGame();
            timer.restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}