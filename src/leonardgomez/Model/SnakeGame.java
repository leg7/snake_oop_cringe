package leonardgomez.Model;

import leonardgomez.View.ViewSnakeGame;

public class SnakeGame extends Game {
	InputMap im;

	public SnakeGame(int turnMax, long sleepDelay, InputMap im) {
		super(turnMax, sleepDelay);
		im = im;
	}

	final protected void initializeGame() {
		// im.getStart_snakes();
	}

	final protected void gameOver() {
		return;
	}

	final protected void takeTurn() {
		return;
	}

	final protected boolean gameContinue() {
		return true;
	}

}
