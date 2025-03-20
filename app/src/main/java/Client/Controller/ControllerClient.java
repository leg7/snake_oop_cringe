package Client.Controller;

import Client.View.ViewSnakeGame;
import Client.View.PanelSnakeGame;
import Server.Model.InputMap;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

public class ControllerClient implements PropertyChangeListener {
	private ViewSnakeGame viewSnakeGame;
	private Gson gson;
	private Socket socket;

	public ControllerClient() {
		gson = new Gson();
		var im = inputMap("layouts/arenaNoWall.lay");

		var p = new PanelSnakeGame(
				im.getSizeX(), im.getSizeY(),
				im.get_walls(),
				im.getStart_snakes(), im.getStart_items());
		viewSnakeGame = new ViewSnakeGame(p);
		viewSnakeGame.addPropertyChangeListener(this);
	}

	public void start(String serverIP, int port) throws IOException {
		socket = new Socket(serverIP, port);

		DataInputStream soIn = new DataInputStream(socket.getInputStream());
		PrintWriter soOut = new PrintWriter(socket.getOutputStream(), true);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msg;
					while ((msg = soIn.readUTF()) != null && !Thread.interrupted()) {
						System.out.println(msg);
					}
				} catch (IOException e) {
				}
			}
		});
		t.start();

		try {
			t.join();
		} catch (InterruptedException e) {
		}

		socket.close();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getPropertyName() + " " + evt.getNewValue());
		try {
			switch (evt.getPropertyName()) {
				case "action":
					if (socket != null && !socket.isClosed()) {
						PrintWriter soOut = new PrintWriter(socket.getOutputStream(), true);
						soOut.println((String) evt.getNewValue());
					}
			}
		} catch (IOException e) {
		}
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
