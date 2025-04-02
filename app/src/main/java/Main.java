import Client.Controller.*;
import Server.Controller.*;
import Server.Lobby.ConnectionHandle;
import Server.Model.*;

import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.Vector;

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
			ServerSocket serveurSocket;
			ConnectionHandle connectionHandle;

			try {
				serveurSocket = new ServerSocket(port);
				System.out.println("Serveur mis en place sur le port " + port + ".");

				connectionHandle = new ConnectionHandle(serveurSocket);

				Thread thread = new Thread(connectionHandle);
				thread.start();

				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (args[0].equals("client")) {
			try {
				ControllerClient controllerClient = new ControllerClient(ip, port);
				controllerClient.listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			badArg();
		}
	}
}
