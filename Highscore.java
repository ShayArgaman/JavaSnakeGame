package SnakeGame;

import java.io.*;

public class Highscore {
    private String filePath;

    public Highscore(String filePath) {
        this.filePath = filePath;
    }

    public int readHighScore() {
        int highscore = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                highscore = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highscore;
    }

    public void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
