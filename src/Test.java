class Test {
	final public static void main(String args[]) {
		ViewSimpleGame v = null;
		SimpleGame g = new SimpleGame(10, 500, v);
		v = new ViewSimpleGame(g);
		ViewCommand viewCommand = new ViewCommand(g);
		g.launch();
	}
}
