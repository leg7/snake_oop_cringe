import Client.Controller.*;

import java.io.IOException;

public class Main {
	final static int ERROR_BAD_ARG = 1;
	final static int port = 8080;
	final static String ip = "127.0.0.1";

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
			ControllerClient c = new ControllerClient();
			try {
				c.start(ip, port);
			} catch (IOException e) { }
		} else {
			badArg();
		}
	}
}
