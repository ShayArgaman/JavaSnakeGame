package SnakeGame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Highscore {
	private int score;
	
	public Highscore() {
		this.score=0;
	}
	
	public int readHighScore() throws FileNotFoundException{

		try {
			Scanner readScore = new Scanner(new File("C:\\Users\\User\\eclipse-workspace\\firstyear\\MyProject\\SnakeGame\\highscore.txt"));
			while(readScore.hasNextLine()) {
				int oldHighScore = readScore.nextInt();
				if(this.score<oldHighScore) {
					this.score=oldHighScore;
				}
			}
			readScore.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return this.score;
	}
	
	public void updateHighScore(int score) {
	    // Only update the high score if the new score is greater
	    if (score > this.score) {
	        try {
	            BufferedWriter writeScore = new BufferedWriter(new FileWriter("C:\\Users\\User\\eclipse-workspace\\firstyear\\MyProject\\SnakeGame\\highscore.txt"));
	            writeScore.write(String.valueOf(score)); // Write the new high score
	            writeScore.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

}

//public void updateHighScore(int newScore) {
//    if (score < newScore) {
//        score = newScore;
//        try {
//            // Update the high score file
//            java.nio.file.Files.write(new File("highscore.txt").toPath(), String.valueOf(newScore).getBytes());
//        } catch (Exception e) {
//            System.out.println("Error updating high score: " + e.getMessage());
//        }
//    }
//}
//
//public int getScore() {
//    return score;
//}
//
//}

