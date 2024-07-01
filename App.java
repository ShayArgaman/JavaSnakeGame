package SnakeGame;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class App {

	public static void main(String[] args) {
		int boardWidth = 600;
		int boardHeight = boardWidth;
		
		JFrame frame = new JFrame("Snake Game");
		frame.setSize(boardWidth,boardHeight);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	    
//		ImageIcon  imgIcon;
//		JLabel lbl = new JLabel();
//	    imgIcon = new ImageIcon(appleImage);
//
//		lbl.setIcon(imgIcon);
//		frame.getContentPane().add(lbl, BorderLayout.CENTER);
		
		
		SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
		frame.add(snakeGame);
		frame.pack();
		snakeGame.requestFocus();
	}

}
