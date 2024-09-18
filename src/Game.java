import java.lang.Runnable;

abstract class Game implements Runnable {
	protected Thread thread;
	protected int turn, turnMax;
	protected boolean isRunning;
	protected long sleepDelay;

	abstract protected void initializeGame();
	public void init() {
		turn = 0;
		turnMax = 0;
		isRunning = true;
		sleepDelay = 0;
		initializeGame();
	}

	public Game(int turnMax, long sleepDelay) {
		init();
		this.turnMax = turnMax;
		this.sleepDelay = sleepDelay;
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
			try {
				Thread.sleep(sleepDelay);
			} catch (Exception e) {}
		}
	}

	public void launch() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
}
