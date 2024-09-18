class Test {
	final public static void main(String args[]) {
		ViewSimpleGame v = new ViewSimpleGame();
		ViewCommand viewCommand = new ViewCommand();
		SimpleGame g = new SimpleGame(10, 500, v);
		g.launch();
	}
}
