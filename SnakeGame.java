package SnakeGame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{
	private class Tile {
		int x;
		int y;

		Tile(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	int boardWidth;
	int boardHeight;
	int tileSize = 25;
	int speed = 100;
	private int score;

	//Snake
	Tile snakeHead;
	ArrayList<Tile> snakeBody;
	ArrayList<Tile> mineBody;

	//Food
	Tile food;
	Tile mine;
	Tile godmode;
	Tile capture;
	Random random;

	//images
	BufferedImage appleImage;
	BufferedImage mineImage;

	//game logic
	Timer gameLoop;
	int velocityX;
	int velocityY;
	boolean gameOver = false;
	boolean paused = false;
	protected int highscore;
	boolean godmodeOn = false;




	SnakeGame(int boardWidth, int boardHeight){
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
		setBackground(Color.black);
		addKeyListener(this);
		setFocusable(true);

		snakeHead = new Tile(5, 5);
		snakeBody = new ArrayList<Tile>();
		mineBody = new ArrayList<Tile>();

		food = new Tile(10,10);
		mine = new Tile(15,15);
		godmode = new Tile(5,5);
		capture = new Tile(0,0);
		random = new Random();
		placeFood();
		placeMine();


		velocityX = 0;
		velocityY = 1;

		gameLoop = new Timer(speed, this);
		gameLoop.start();

		//highscore - read
		Highscore readscore = new Highscore();
		try {
			highscore=readscore.readHighScore(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//highscore - update
		Highscore updatescore = new Highscore();
		if(score<highscore) {
			updatescore.updateHighScore(score);
		}
		
        try {
            appleImage = ImageIO.read(new File("C:\\Users\\User\\eclipse-workspace\\firstyear\\MyProject\\SnakeGame\\apple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
        	mineImage = ImageIO.read(new File("C:\\Users\\User\\eclipse-workspace\\firstyear\\MyProject\\SnakeGame\\mine.png"));
        }	catch (IOException e) {
        	e.printStackTrace();
        }
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		//Grid
		for (int i = 0 ; i < boardWidth/tileSize ; i++) {
			//(x1, y1, x2, y2)
			g.drawLine(i*tileSize, 0, i*tileSize, boardHeight); // x
			g.drawLine(0, i*tileSize, boardWidth, i*tileSize); // y
		}

		//		//Capture
		//		for(int i=0 ; i<boardWidth/tileSize ; i++) {
		//			g.setColor(Color.lightGray); // (x1,y1,x2,y2)
		//			g.fill3DRect(i-tileSize, 0 , i*tileSize+tileSize, tileSize/2, true); // y
		//			g.fill3DRect(0 , i-tileSize/2 , tileSize/2 , i*tileSize+tileSize, true); // x
		//		}

		//Food
        g.drawImage(appleImage, food.x * tileSize, food.y * tileSize, tileSize, tileSize, null);

		//Mines
		g.drawImage(mineImage, mine.x * tileSize, mine.y * tileSize, tileSize, tileSize, null);
		
		//godmode
		if(godmodeOn) {
			g.setColor(Color.cyan);
			g.fill3DRect(godmode.x * tileSize, godmode.y * tileSize, tileSize, tileSize,true);
		}
		//Snake Head
		g.setColor(Color.green);
		g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

		//Snake Body
		for(int i=0 ; i < snakeBody.size() ; i++){
			Tile snakePart = snakeBody.get(i);
			g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
		}

		//Score
		g.setFont(new Font("Arial", Font.PLAIN, 25));
		if (gameOver) {
			g.setColor(Color.red);
			g.drawString("Game Over", boardWidth/2-60, boardHeight/2-100);
			g.drawString("Score: " + String.valueOf(snakeBody.size()) , boardWidth/2-45, boardHeight/2-55);
		}
		else {
			g.setFont(new Font("Arial", Font.PLAIN, 16));
			g.drawString("Score: "+ String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
		}
		//High Score
		if(!gameOver) {
			g.setFont(new Font("Ariel", Font.PLAIN, 14));
			g.setColor(Color.ORANGE);
			g.drawString("High Score: " + highscore, tileSize-14, tileSize+18);
		}
		else {
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(Color.green);
			g.drawString("High Score: " + highscore, boardWidth/2-55, boardHeight/5);
		}

		// P - for pause
		if(paused && !gameOver) { // Enter to continue
		    g.setFont(new Font("Arial", Font.PLAIN, 36));
		    g.setColor(Color.white);
		    g.drawString("Press Enter to continue", boardWidth/4-tileSize , boardHeight/2-tileSize);
		}
		else if (!paused && !gameOver) { 
		    g.setFont(new Font("Arial", Font.PLAIN, 13));
		    g.setColor(Color.white);
		    g.drawString("Press 'P' for Pause", boardWidth / 2 + tileSize * 7, tileSize);
		}  

		if(gameOver) { // Press Enter to Start
			g.setColor(Color.white);
			g.setFont(new Font("Ariel",Font.PLAIN,30));
			g.drawString("Press Enter to Start", boardWidth/2-135,boardHeight/2);
			return;
		}
	}

	private void gamePause() {
		paused=true;
		repaint();
	}

	private void gameResumed() {
		paused=false;
	}

	
	public void placeFood() {
		food.x = random.nextInt(boardHeight/tileSize);
		food.y = random.nextInt(boardWidth/tileSize); //600/25 = 24
	}

	public void placeMine() {
		do {
			if(velocityX==0)  //if i getting up/down - the mine can't create in the snake face's
				mine.y=random.nextInt(boardHeight/tileSize);
			
			else if(velocityY==0)  //if i getting left/right 
				mine.x=random.nextInt(boardWidth/tileSize);
		} while(collision(food, mine) ||collision(snakeHead, mine));
	}
	
	public void godmode() {
		int godmodeRandom = random.nextInt(2); //generating number between 0->
			if(godmodeRandom==score) {
				do {
				godmode.x=random.nextInt(boardWidth/tileSize);
				godmode.y=random.nextInt(boardHeight/tileSize);
				}
				while(collision(food,godmode) && collision(mine,godmode));
				
				if(snakeHead.x==godmode.x && snakeHead.y==godmode.y) 
					godmodeOn = true;
			}
			else
				godmodeOn = false;
	}
		

	public boolean collision(Tile tile1, Tile tile2) {
		return tile1.x == tile2.x && tile1.y == tile2.y;
	}

	public void move() {
		//eat food
		if(collision(snakeHead, food)) {
			snakeBody.add(new Tile(food.x, food.y));
			placeFood();
			placeMine();
			godmode();
			
			// Increase speed every 2nd time the snake eats food
			if(speed>50 && snakeBody.size()%2==0) {
				speed -= 5; 
				gameLoop.setDelay(speed);
			}
		}

		//eat mine
		if(collision(snakeHead,mine)) {
			gameOver=true;
		}

		//Snake Body
		for(int i = snakeBody.size()-1 ; i>=0 ; i--) {
			Tile snakePart = snakeBody.get(i);
			if (i==0 ) {
				snakePart.x = snakeHead.x;
				snakePart.y = snakeHead.y;
			}
			else {
				Tile prevSnakePart = snakeBody.get(i-1);
				snakePart.x = prevSnakePart.x;
				snakePart.y = prevSnakePart.y;
			}
		}

		//Snake Head
		snakeHead.x += velocityX;
		snakeHead.y += velocityY;

		//game over conditions
		for (int i = 0 ; i <snakeBody.size() ; i++) {
			Tile snakePart = snakeBody.get(i);
			//collide with the snake head
			if(collision(snakeHead, snakePart) && !godmodeOn) {
				gameOver = true;
			}
		}

		//		if(snakeHead.x*tileSize <= 0 || snakeHead.x*tileSize >= boardWidth || 
		//				snakeHead.y*tileSize <= 0 || snakeHead.y*tileSize >= boardHeight) {
		//			gameOver= true;
		//		}


		//LEVELS
		//		gameLoop(5);
		//	    if (snakeBody.size() > mineBody.size()) {
		//	    	for(int i=mineBody.size()-1 ; i >= 0 ; i--) {
		//	    		Tile minePart = mineBody.get(i);
		//	    		mineBody.add(new Tile(mine.x, mine.y));
		//	    		placeMine();
		//	    	}
		//	    }

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
		move();
		repaint();
		if(gameOver) {
			gameLoop.stop();
		}
	}

	private void restartGame() {
		gameOver = false;
		snakeHead = new Tile(5, 5);
		snakeBody.clear();
		placeFood();
		placeMine();
		velocityX = 0;
		velocityY = 1;
		speed = 100; // Reset speed if needed
		gameLoop.setDelay(speed);
		score = 0;
		gameLoop.start();
		gameResumed();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_P) {
			gameLoop.stop();
			gamePause();
		}

		// If Enter is pressed, start or restart the game
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (gameOver) {
				restartGame();
			} 
			else {
				gameLoop.start();
				gameResumed();
			}
		}


		if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
			velocityX = 0;
			velocityY = -1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
			velocityX = 0;
			velocityY = 1;
		}
		else if(e.getKeyCode() == (KeyEvent.VK_LEFT) && velocityX != 1) {
			velocityX = -1;
			velocityY = 0;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
			velocityX = 1;
			velocityY = 0;
		}

		else if(e.getKeyCode() == KeyEvent.VK_D && velocityX != -1) { //right
			velocityX = 1;
			velocityY = 0;
		}

		else if(e.getKeyCode() == (KeyEvent.VK_A) && velocityX != 1) { // left
			velocityX = -1;
			velocityY = 0;
		}
		else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1) { // down
			velocityX = 0;
			velocityY = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_W && velocityY != 1) { // up
			velocityX = 0;
			velocityY = -1;
		}


	}

	// do not need
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}


}
