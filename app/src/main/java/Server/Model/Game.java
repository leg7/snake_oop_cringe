package Server.Model;

import java.lang.Runnable;
import java.beans.*;

public abstract class Game implements Runnable {
	protected Thread thread;
	protected int turn, turnMax;
	protected boolean isRunning;
	protected long sleepDelay;
	protected final PropertyChangeSupport pcs;

	abstract protected void initializeGame();
	public void init() {
		turn = 0;
		isRunning = false;
		initializeGame();
	}

	public Game(int turnMax, long sleepDelay) {
		this.turnMax = turnMax;
		this.sleepDelay = sleepDelay;
		pcs = new PropertyChangeSupport(this);
	}

	abstract protected void takeTurn();
	abstract protected boolean gameContinue();
	abstract protected void gameOver();

	public void step() {
		if (gameContinue() && turn < turnMax) {
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


	// Property method
	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(property, listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(property, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public int getTurn() { return turn; }

	public void setSleepDelay(long speed) {
		sleepDelay = speed;
	}

}
