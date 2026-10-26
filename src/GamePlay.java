import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private Timer timer;
    private int delay = 8;

    private MapGenerator map;
    private int totalBricks = 0;

    private int score = 0;
    private boolean isFinished = false;
    private int currentLevel = 1;

    // --- NOU: Variabile pentru timpul maxim de joc ---
    private int timeRemaining = 60; // 60 de secunde la dispoziție
    private Timer countdownTimer;

    public GamePlay() {
        currentLevel = DatabaseManager.getUserLevel(Main.currentUser);
        setupLevel();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer = new Timer(delay, this);
        timer.start();

        // --- NOU: Inițializăm ceasul care scade timpul cu o secundă ---
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play && !isFinished) {
                    timeRemaining--;
                    if (timeRemaining <= 0) {
                        isFinished = true;
                        play = false;
                    }
                    repaint(); // Reîmprospătăm ecranul pentru a afișa noul timp
                }
            }
        });
        countdownTimer.start();
    }

    private void setupLevel() {
        File mapFile = new File("nivel" + currentLevel + ".txt");

        if (!mapFile.exists()) {
            currentLevel = 1;
            delay = delay - 2;
            if (delay < 2) delay = 2;
            if (timer != null) timer.setDelay(delay);
        }

        map = new MapGenerator("nivel" + currentLevel + ".txt");
        totalBricks = map.getTotalBricks();

        // --- NOU: Resetăm timpul la începutul fiecărui nivel ---
        timeRemaining = 60;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g);

        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Afișăm scorul, Nivelul și Timpul pe ecran
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Nivel: " + currentLevel, 30, 30);

        // --- NOU: Afișăm cronometrul pe mijloc ---
        if (timeRemaining <= 10) {
            g.setColor(Color.RED); // Se face roșu când mai ai puțin timp
        }
        g.drawString("Timp: " + timeRemaining + "s", 310, 30);

        g.setColor(Color.WHITE);
        g.drawString("Scor: " + score, 570, 30);

        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.YELLOW);
        g.fillOval(ballposX, ballposY, 20, 20);

        // Nivel Terminat (Victorie)
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.GREEN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Nivel Terminat! Scor: " + score, 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Apasă ENTER pentru a avansa", 200, 350);
            isFinished = true;
        }

        // Game Over (dacă bila cade)
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over! Scor: " + score, 200, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Apasă ENTER pentru a reîncerca nivelul", 160, 350);
            isFinished = true;
        }

        // --- NOU: Game Over (dacă expiră timpul) ---
        if (timeRemaining <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Timpul a expirat! Scor: " + score, 160, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Apasă ENTER pentru a reîncerca", 180, 350);
            isFinished = true;
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play && !isFinished) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + rect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play && isFinished) {
                DatabaseManager.updateScore(Main.currentUser, score);

                if (totalBricks <= 0) {
                    currentLevel++;
                    DatabaseManager.updateLevel(Main.currentUser, currentLevel);
                }

                play = true;
                isFinished = false;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;

                setupLevel();
                repaint();
            }
        }
    }

    public void moveRight() {
        if(!isFinished) play = true;
        playerX += 20;
    }

    public void moveLeft() {
        if(!isFinished) play = true;
        playerX -= 20;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}