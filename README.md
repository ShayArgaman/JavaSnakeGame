# Java Snake Game

Developed a Java-based Snake Game with Swing, incorporating modern features like mines, a temporary invincibility mode (god mode), and a dynamic high score tracking system. The game delivers an enhanced, engaging experience with progressively challenging levels, offering a fresh twist on the classic gameplay.

<img src="https://github.com/user-attachments/assets/55714918-006e-4a17-8ab5-11416f4797d9" width="400"/>

## Features

- Classic snake gameplay with increasing difficulty.
- Mines that the snake must avoid.
- God mode for temporary invincibility.
- High score tracking.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- An IDE or text editor (e.g., Eclipse, IntelliJ IDEA, VSCode)

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/ShayArgaman/JavaSnakeGame.git
    ```

2. Navigate to the project directory:
    ```bash
    cd JavaSnakeGame
    ```

3. Open the project in your preferred IDE.

### Running the Game

1. Ensure the `highscore.txt` file is present in the project root directory.
2. Ensure the images (`apple.png` and `mine.png`) are present in the `images` directory.
3. Run the `App.java` file.

### Controls

- **Arrow Keys / WASD**: Move the snake
- **P**: Pause the game
- **Enter**: Start or restart the game

## Project Structure

- `src/SnakeGame/`: Contains the Java source files for the game.
  - `App.java`: The main entry point for the game.
  - `Highscore.java`: Handles reading and updating the high score.
  - `SnakeGame.java`: Contains the game logic and rendering.
  - `apple.java`: (Optional) A separate JFrame to display the apple image.
- `images/`: Contains the image files used in the game.
  - `apple.png`: The image for the food.
  - `mine.png`: The image for the mines.
- `highscore.txt`: Stores the high score.
- `README.md`: This file.
- `.gitignore`: Specifies files and directories to be ignored by Git.


## Acknowledgments

- Inspired by the classic Snake game.
- Thanks to the open-source community for various resources and tools.
