public class Main {
	static int ERROR_BAD_ARG = 1;

	public static void badArg() {
		System.err.println("Please run with \"server\" or \"client\" argument :)");
		System.exit(ERROR_BAD_ARG);
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			badArg();
		}

		if (args[0].equals("server")) {

		} else if (args[0].equals("client")) {

		} else {
			badArg();
		}
	}
}
