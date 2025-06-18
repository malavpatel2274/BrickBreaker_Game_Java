/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author malav
 */

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 48;

    private final Timer timer;
    private final int delay = 8;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private Clip hitBrickSound;
    private Clip wallHitSound;
    private Clip paddleHitSound;
    private Clip gameOverSound;

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(4, 12);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        loadSounds();
    }

    private void loadSounds() {
        try {
            // Load hitBrickSound
            AudioInputStream hitBrickAudioInputStream = AudioSystem.getAudioInputStream(new File("src\\wav\\hitBrickSound.wav"));
            hitBrickSound = AudioSystem.getClip();
            hitBrickSound.open(hitBrickAudioInputStream);

            // Load wallHitSound
            AudioInputStream wallHitAudioInputStream = AudioSystem.getAudioInputStream(new File("src\\wav\\wallHitSound.wav"));
            wallHitSound = AudioSystem.getClip();
            wallHitSound.open(wallHitAudioInputStream);
            
            // Load gameOverSound
            AudioInputStream gameOverAudioInputStream = AudioSystem.getAudioInputStream(new File("src\\wav\\gameOverSound.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOverAudioInputStream);

            // Adjust volume (from 0.0 to 1.0)
            FloatControl volumeControl = (FloatControl) hitBrickSound.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f);

            // Load paddleHitSound
            AudioInputStream paddleHitAudioInputStream = AudioSystem.getAudioInputStream(new File("src\\wav\\paddleHitSound.wav"));
            paddleHitSound = AudioSystem.getClip();
            paddleHitSound.open(paddleHitAudioInputStream);

            // Set looping (true or false)
            hitBrickSound.loop(1); // Set to loop once
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // Use paintComponent instead of paint
        super.paintComponent(g);

        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // drawing map with random colors
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.black);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // the scores
        g.setColor(Color.yellow);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.blue);
        g.fillOval(ballposX, ballposY, 20, 20);

        // when you won the game
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 260, 300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230, 350);
            
            // Play game over sound
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }

        // when you lose the game
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " + score, 190, 300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230, 350);

            // Play game over sound
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }

        g.dispose();
    }

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
            if (!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8))) {
                ballYdir = -ballYdir;
                ballXdir = -2;

                // Play paddle hit sound
                paddleHitSound.setFramePosition(0);
                paddleHitSound.start();
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
                ballYdir = -ballYdir;
                ballXdir = ballXdir + 1;

                // Play paddle hit sound
                paddleHitSound.setFramePosition(0);
                paddleHitSound.start();
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8))) {
                ballYdir = -ballYdir;

                // Play paddle hit sound
                paddleHitSound.setFramePosition(0);
                paddleHitSound.start();
            }

            // check map collision with the ball
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            totalBricks--;

                            // when ball hit right or left of brick
                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            }

                            // when ball hits top or bottom of brick
                            else {
                                ballYdir = -ballYdir;
                            }

                            // Play hit brick sound
                            hitBrickSound.setFramePosition(0);
                            hitBrickSound.start();
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0) {
                ballXdir = -ballXdir;
                // Play game over sound
                wallHitSound.setFramePosition(0);
                wallHitSound.start();
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
                // Play game over sound
                wallHitSound.setFramePosition(0);
                wallHitSound.start();
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
                // Play game over sound
                wallHitSound.setFramePosition(0);
                wallHitSound.start();
            }

            repaint();
        }
    }
}
