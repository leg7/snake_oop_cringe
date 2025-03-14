package Client.Controller;

import Client.View.ViewSnakeGame;
import Client.View.PanelSnakeGame;
import Server.Model.InputMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class ControllerClient {
	private ViewSnakeGame viewSnakeGame;

	public ControllerClient() {
		var im = inputMap("layouts/arenaNoWall.lay");

		var p = new PanelSnakeGame(
				im.getSizeX(), im.getSizeY(),
				im.get_walls(),
				im.getStart_snakes(), im.getStart_items());
		viewSnakeGame = new ViewSnakeGame(p);
	}

	public void start(String serverIP, int port) throws IOException {
		Socket so = new Socket(serverIP, port);

		BufferedReader soIn = new BufferedReader(new InputStreamReader(so.getInputStream()));
		PrintWriter soOut = new PrintWriter(so.getOutputStream(), true);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msg;
					while ((msg = soIn.readLine()) != null && !Thread.interrupted()) {
						System.out.println(msg);
					}
				} catch (IOException e) {
				}
			}
		});
		t.start();

		// while (/* View alive */) {
		//
		// }

		t.interrupt();

		so.close();
	}

	public InputMap inputMap(String filename) {
		try {
			return new InputMap(filename);
		} catch (Exception e) {
			System.out.println("Couldn't import layout file");
			System.exit(69);
		}
		return null;
	}
}
