public abstract class AbstractController {
	protected Game g;

	public void restart() {
		g.init();
	}

	public void step() {
		g.step();
	}

	public void play() {
		g.launch();
	}

	public void pause() {
		g.pause();
	}

	public void setSpeed(int speed) {
		g.setSleepDelay(1000 / speed);
	}
}
