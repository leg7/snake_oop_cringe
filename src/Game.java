import java.lang.Runnable;

abstract class Game implements Runnable {
	protected Thread thread;
	protected int turn, turnMax;
	protected boolean isRunning;
	protected long sleepDelay;
	protected ViewSimpleGame view;

	abstract protected void initializeGame();
	public void init() {
		turn = 0;
		turnMax = 0;
		isRunning = true;
		sleepDelay = 0;
		initializeGame();
	}

	public Game(int turnMax, long sleepDelay, ViewSimpleGame view) {
		init();
		this.turnMax = turnMax;
		this.sleepDelay = sleepDelay;
		this.view = view;
	}

	abstract protected void takeTurn();
	abstract protected boolean gameContinue();
	abstract protected void gameOver();
	public void step() {
		if (gameContinue() && turn < turnMax) {
			++turn;
			takeTurn();
			view.updateTurnCounter(turn);
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
