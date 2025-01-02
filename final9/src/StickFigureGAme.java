import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StickFigureGAme extends JPanel implements ActionListener, KeyListener {

    // Karakter özellikleri
    private int playerX = 100, playerY = 400, playerWidth = 30, playerHeight = 50;
    private int velocityY = 0;
    private boolean isJumping = false;

    // Hareket özellikleri
    private boolean moveLeft = false, moveRight = false;

    // Engeller
    private ArrayList<Rectangle> obstacles;

    // Zamanlayıcı (Game Loop)
    private Timer timer;

    public StickFigureGAme() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        obstacles = new ArrayList<>();

        // Başlangıçta bir kaç engel ekleyelim
        for (int i = 0; i < 5; i++) {
            obstacles.add(new Rectangle(800 + i * 200, 400, 30, 30)); // Engellerin başlangıç pozisyonları
        }

        timer = new Timer(16, this); // 60 FPS
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Karakteri çiz
        g2d.setColor(Color.BLUE);
        drawStickFigure(g2d, playerX, playerY);

        // Engelleri çiz
        g2d.setColor(Color.RED);
        for (Rectangle obstacle : obstacles) {
            g2d.fill(obstacle);
        }

        // Oyun bitti ise mesaj göster
        for (Rectangle obstacle : obstacles) {
            if (playerX + playerWidth > obstacle.x && playerX < obstacle.x + obstacle.width && playerY + playerHeight > obstacle.y) {
                gameOver(g2d);
                return;
            }
        }

        // Engellerin hareketini güncelle
        moveObstacles();
    }

    private void drawStickFigure(Graphics2D g2d, int x, int y) {
        // Karakteri çizen çizgiler (stick figure)
        g2d.setStroke(new BasicStroke(3));

        // Baş (daire)
        g2d.drawOval(x + 5, y -60, 20, 20);

        // Vücut
        g2d.drawLine(x + 15, y, x + 15, y - 40);

        // Kollar
        g2d.drawLine(x + 15, y - 20, x, y - 30);  // Sol kol
        g2d.drawLine(x + 15, y - 20, x + 30, y - 30);  // Sağ kol

        // Bacaklar
        g2d.drawLine(x + 15, y, x, y + 20);  // Sol bacak
        g2d.drawLine(x + 15, y, x + 30, y + 20);  // Sağ bacak
    }

    private void moveObstacles() {
        for (Rectangle obstacle : obstacles) {
            obstacle.x -= 5; // Engelleri sola hareket ettir

            // Eğer engel ekranın dışına çıkarsa, yeniden sağdan gelmesini sağla
            if (obstacle.x + obstacle.width < 0) {
                obstacle.x = 800;
            }
        }
    }

    private void gameOver(Graphics2D g2d) {
        String message = "Game Over!";
        Font font = new Font("Arial", Font.BOLD, 50);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(message, getWidth() / 2 - 150, getHeight() / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Karakterin hareketini güncelle
        if (moveLeft) {
            playerX -= 5;
        }
        if (moveRight) {
            playerX += 5;
        }
        if (isJumping) {
            velocityY += 1; // Yer çekimi
            playerY += velocityY;

            if (playerY >= 400) {
                playerY = 400;
                velocityY = 0;
                isJumping = false;
            }
        }

        repaint(); // Ekranı güncelle
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }

        if (key == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }

        if (key == KeyEvent.VK_SPACE && !isJumping) {
            isJumping = true;
            velocityY = -15; // Zıplama gücü
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }

        if (key == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stick Figure Game");
        StickFigureGAme gamePanel = new StickFigureGAme();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}
