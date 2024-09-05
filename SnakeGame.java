package SnakeGame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    int speed = 100;
    private int score;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    ArrayList<Tile> mineBody;

    // Food, Mine and Godmode
    Tile food;
    Tile mine;
    Tile godmode;
    Random random;

    // Images
    BufferedImage appleImage;
    BufferedImage mineImage;
    BufferedImage godmodeImage;

 
    // Game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;
    boolean paused = false;
    protected int highscore;
    boolean godmodeOn = false;
    long godmodeStartTime;
    int godmodeDuration = 10000; // Godmode lasts 10 seconds
    private boolean newHighScore = false;
    private long highScoreMessageStart = 0;
    private final int HIGH_SCORE_MESSAGE_DURATION = 3000; // Display for 3 seconds
    
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        mineBody = new ArrayList<>();

        food = new Tile(10, 10);
        mine = new Tile(15, 15);
        godmode = new Tile(10, 10);
        random = new Random();
        placeFood();
        placeMine();

        velocityX = 0;
        velocityY = 1;

        gameLoop = new Timer(speed, this);
        gameLoop.start();

        // Highscore - read
    	String highscoreResourcePath = System.getProperty("user.dir") + File.separator 
    	        + "MyProject" + File.separator + "SnakeGame" + File.separator + "highscore.txt";
    	
        Highscore highscoreManager = new Highscore(highscoreResourcePath);
        highscore = highscoreManager.readHighScore();

        try {
        	String resourcePath = System.getProperty("user.dir") + File.separator 
        	        + "MyProject" + File.separator + "SnakeGame" + File.separator + "images" + File.separator + "apple.png";
                        
            File file = new File(resourcePath);
            
            appleImage = ImageIO.read(file); 
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
        	String resourcePath = System.getProperty("user.dir") + File.separator 
        	        + "MyProject" + File.separator + "SnakeGame" + File.separator + "images" + File.separator+ "mine.png";
                        
            File file = new File(resourcePath);
            
            mineImage = ImageIO.read(file); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
        	String resourcePath = System.getProperty("user.dir") + File.separator 
        	        + "MyProject" + File.separator + "SnakeGame" + File.separator + "images" + File.separator + "godmode.png";
        	
            File file = new File(resourcePath);

        	godmodeImage = ImageIO.read(file); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // x
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize); // y
        }

        // Food
        g.drawImage(appleImage, food.x * tileSize, food.y * tileSize, tileSize, tileSize, null);

        // Mines
        g.drawImage(mineImage, mine.x * tileSize, mine.y * tileSize, tileSize, tileSize, null);

        // Godmode
        if (!godmodeOn && godmode != null) {
            g.drawImage(godmodeImage, godmode.x * tileSize, godmode.y * tileSize, tileSize, tileSize, null);
        }
        // Display Godmode timer
        if (godmodeOn) {
            int remainingTime = (int) ((godmodeDuration - (System.currentTimeMillis() - godmodeStartTime)) / 1000);
            g.setColor(new Color(0, 255, 255, 180)); // Semi-transparent cyan
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Godmode: " + remainingTime + "s", boardWidth - 150, 50); // Adjusted position
        }

        if (newHighScore && System.currentTimeMillis() - highScoreMessageStart < HIGH_SCORE_MESSAGE_DURATION) {
            drawHighScoreMessage(g);
        }
        // Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over", boardWidth / 2 - 60, boardHeight / 2 - 100);
            g.drawString("Score: " + snakeBody.size(), boardWidth / 2 - 45, boardHeight / 2 - 55);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
        }

        // High Score
        if (!gameOver) {
            g.setFont(new Font("Ariel", Font.PLAIN, 14));
            g.setColor(Color.ORANGE);
            g.drawString("High Score: " + highscore, tileSize - 14, tileSize + 18);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.green);
            g.drawString("High Score: " + highscore, boardWidth / 2 - 55, boardHeight / 5);
        }

        // Pause Message
        if (paused && !gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 36));
            g.setColor(Color.white);
            g.drawString("Press Enter to continue", boardWidth / 4 - tileSize, boardHeight / 2 - tileSize);
        } else if (!paused && !gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 13));
            g.setColor(Color.white);
            g.drawString("Press 'P' for Pause", boardWidth / 2 + tileSize * 7, tileSize);
        }

        // game over Message
        if (gameOver) {
            g.setColor(Color.white);
            g.setFont(new Font("Ariel", Font.PLAIN, 30));
            g.drawString("Press Enter to Start", boardWidth / 2 - 135, boardHeight / 2);
        }
    }
    
    private void drawHighScoreMessage(Graphics g) {
        String message = "NEW HIGH SCORE!";
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(new Color(0, 0, 0, 180));
        FontMetrics fm = g.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        int messageHeight = fm.getHeight();
        g.fillRect(boardWidth/2 - messageWidth/2 - 10, boardHeight/2 - messageHeight/2 - 10, 
                   messageWidth + 20, messageHeight + 20);
        g.setColor(Color.YELLOW);
        g.drawString(message, boardWidth/2 - messageWidth/2, boardHeight/2 + messageHeight/4);
        
        //if (System.currentTimeMillis() - highScoreMessageStart > HIGH_SCORE_MESSAGE_DURATION) {
        //    newHighScore = false; // Reset the flag after the message duration
        //}
    }
    
    private void gamePause() {
        paused = true;
        repaint();
    }

    private void gameResumed() {
        paused = false;
    }

    public void placeFood() {
        food.x = random.nextInt(boardHeight / tileSize);
        food.y = random.nextInt(boardWidth / tileSize);
    }

    public void placeMine() {
        do {
            if (velocityX == 0) { // If moving up/down, mine can't be in snake's path
                mine.y = random.nextInt(boardHeight / tileSize);
            } else if (velocityY == 0) { // If moving left/right
                mine.x = random.nextInt(boardWidth / tileSize);
            }
        } while (collision(food, mine) || collision(snakeHead, mine));
    }

    public void placeGodMode() {
        if (godmode == null) {
            godmode = new Tile(0, 0); // Initialize godmode if it's null
        }
        do {
            godmode.x = random.nextInt(boardWidth / tileSize);
            godmode.y = random.nextInt(boardHeight / tileSize);
        } while (collision(food, godmode) || collision(snakeHead, godmode) || collision(mine, godmode));
    }
    

    public void activateGodmode() {
        godmodeOn = true;
        godmodeStartTime = System.currentTimeMillis();

        // Schedule godmode to deactivate and reappear
        new Timer(godmodeDuration, e -> {
            godmodeOn = false;
            ((Timer) e.getSource()).stop();
            scheduleGodmodeReappearance();
        }).start();
    }
    
 // Schedule godmode to reappear at a random time
    private void scheduleGodmodeReappearance() {
        int randomTime = random.nextInt(15000) + 5000; // 5 to 20 seconds
        new Timer(randomTime, e -> {
            placeGodMode();
            ((Timer) e.getSource()).stop();
        }).start();
    }
    
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() { 
        // Check for Godmode collision
        if (godmode != null && collision(snakeHead, godmode)) {
            placeGodMode();
            activateGodmode();
            godmode = null; // Remove the godmode item
        }
        
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
            placeMine();
            
            // Increase speed every 2nd time the snake eats food
            if (speed > 50 && snakeBody.size() % 2 == 0) {
                speed -= 5;
                gameLoop.setDelay(speed);
            }
        }

        // Check for mine collision only if not in godmode
        if (collision(snakeHead, mine) && !godmodeOn) {
            gameOver = true;
        }

        // Snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over conditions
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart) && !godmodeOn) {
                gameOver = true;
                break;
            }
        }

        // Snake hits borders
		if(snakeHead.y*tileSize < 0 ) {
			snakeHead.y=boardHeight / tileSize - 1 ;
		}
		else if(snakeHead.y*tileSize==boardHeight) {
			snakeHead.y=0;
		}

		if(snakeHead.x*tileSize==boardWidth) {
			snakeHead.x=0;
		}
		else if(snakeHead.x*tileSize < 0) {
			snakeHead.x=boardWidth / tileSize - 1;
		}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            move();
        }

        // Check and save new high score
        if (snakeBody.size() > highscore) {
        	if(!newHighScore) {
                highScoreMessageStart = System.currentTimeMillis();
        	}
            highscore = snakeBody.size();
            Highscore highscoreManager = new Highscore("C:\\Users\\User\\eclipse-workspace\\firstyear\\MyProject\\SnakeGame\\highscore.txt");
            highscoreManager.saveHighScore(highscore);
            newHighScore = true;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !paused) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (velocityY != 1) {
                        velocityX = 0;
                        velocityY = -1;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (velocityY != -1) {
                        velocityX = 0;
                        velocityY = 1;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (velocityX != 1) {
                        velocityX = -1;
                        velocityY = 0;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (velocityX != -1) {
                        velocityX = 1;
                        velocityY = 0;
                    }
                    break;
            }
        }

        // Pause
        if (e.getKeyCode() == KeyEvent.VK_P) {
            gamePause();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
            // Reset game
            snakeHead = new Tile(5, 5);
            snakeBody.clear();
            score = 0;
            speed = 100;
            velocityX = 0;
            velocityY = 1;
            gameOver = false;
            newHighScore = false;
            gameLoop.setDelay(speed);
            placeFood();
            placeMine();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && paused) {
            gameResumed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
