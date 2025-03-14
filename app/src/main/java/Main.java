import Client.Controller.*;
import Server.Controller.*;

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
			//Vector<Socket> clients = new Vector<>();
			ServerSocket serveurSocket;

			try {
				serveurSocket = new ServerSocket(port);
				System.out.println("Serveur mis en place.");

				while (true) {   // le serveur va attendre qu'une connexion arrive
					Socket socket = serveurSocket.accept();
					//clients.add(socket);
					ControllerServer controllerServer = new ControllerServer(socket);

					Thread thread = new Thread(controllerServer);
					thread.start();
				}
			} catch (IOException e) {
				System.err.println("problème\n"+e);
			}

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
