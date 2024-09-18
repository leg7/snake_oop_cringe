abstract class Game {
	protected int turn, turnMax;
	protected boolean isRunning;

	abstract protected void initializeGame();
	public void init() {
		turn = 0;
		isRunning = true;
		initializeGame();
	}

	public Game(int turnMax) {
		this.turnMax = turnMax;
		init();
	}

	abstract protected void takeTurn();
	abstract protected boolean gameContinue();
	abstract protected void gameOver();
	public void step() {
		if (gameContinue() && turn < turnMax) {
			++turn;
			takeTurn();
		} else {
			isRunning = false;
			gameOver();
		}
	}

	public void pause() { isRunning = false; }

	public void run() {
		while (isRunning) {
			step();
		}
	}
}
