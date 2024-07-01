# Java Snake Game

This is a classic Snake Game implemented in Java using Swing. The game includes additional features like mines, a god mode, and a high score tracking system.

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

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests for any enhancements or bug fixes.

1. Fork the repository.
2. Create your feature branch:
    ```bash
    git checkout -b feature/YourFeature
    ```
3. Commit your changes:
    ```bash
    git commit -m 'Add your feature'
    ```
4. Push to the branch:
    ```bash
    git push origin feature/YourFeature
    ```
5. Open a pull request.

## Acknowledgments

- Inspired by the classic Snake game.
- Thanks to the open-source community for various resources and tools.
