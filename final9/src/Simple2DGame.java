import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Simple2DGame extends JPanel implements ActionListener, KeyListener {

    // Player properties
    private int playerX = 100, playerY = 400, playerWidth = 30, playerHeight = 50;
    private int velocityY = 0;
    private boolean isJumping = false;
    private boolean isDodging = false;

    // Movement properties
    private boolean moveLeft = false, moveRight = false;

    // Timer for game loop
    private Timer timer;

    // Obstacle
    private Rectangle obstacle;

    public Simple2DGame() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, this); // 60 FPS
        timer.start();

        // Initialize an obstacle
        obstacle = new Rectangle(600, 400, 50, 50);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw player (stick figure)
        g2d.setColor(Color.BLUE);
        g2d.fillRect(playerX, playerY, playerWidth, playerHeight);

        // Draw obstacle
        g2d.setColor(Color.RED);
        g2d.fill(obstacle);

        // Game Over condition
        if (playerX + playerWidth > obstacle.x && playerX < obstacle.x + obstacle.width && playerY + playerHeight > obstacle.y) {
            gameOver(g2d);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle player movement and physics
        if (moveLeft) {
            playerX -= 5;
        }
        if (moveRight) {
            playerX += 5;
        }
        if (isJumping) {
            velocityY += 1; // gravity
            playerY += velocityY;

            if (playerY >= 400) {
                playerY = 400;
                velocityY = 0;
                isJumping = false;
            }
        }

        // Redraw the screen
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Move left
        if (key == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }

        // Move right
        if (key == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }

        // Jump
        if (key == KeyEvent.VK_SPACE && !isJumping) {
            isJumping = true;
            velocityY = -15; // Jump force
        }

        // Dodge (for simplicity, we'll just move the player quickly to the right for this example)
        if (key == KeyEvent.VK_D) {
            isDodging = true;
            playerX += 20; // Dodge to the right
        }

        // Hit (simple action, we'll just print to console)
        if (key == KeyEvent.VK_H) {
            System.out.println("Player hits!");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Stop movement when the key is released
        if (key == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }

        // Stop dodging when the key is released
        if (key == KeyEvent.VK_D) {
            isDodging = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void gameOver(Graphics2D g2d) {
        String message = "Game Over!";
        Font font = new Font("Arial", Font.BOLD, 50);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(message, getWidth() / 2 - 150, getHeight() / 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple 2D Game");
        Simple2DGame gamePanel = new Simple2DGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}
