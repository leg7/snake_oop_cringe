class SimpleGame extends Game {
	private void log(String message) {
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		System.out.println(methodName + "(): " + message);
	}

	final protected void initializeGame() { }

	public SimpleGame(int turnMax, long sleepDelay) {
		super(turnMax, sleepDelay);
		log("turnMax = " + turnMax + ", sleepDelay = " + sleepDelay);
	}

	@Override final public void init() {
		log("Initializing game");
		super.init();
	}

	final protected void takeTurn() {
		log("turn = " + turn);
	}

	final protected boolean gameContinue() {
		boolean v = true;
		log("" + v);
		return v;
	}

	final protected void gameOver() {
		log("GAME OVER");
	}

	@Override final public void step() {
		log("taking a step");
		super.step();
	}

	@Override final public void pause() {
		super.pause();
		log("" + isRunning);
	}

	@Override final public void run() {
		log("Starting game loop");
		super.run();
	}
}
