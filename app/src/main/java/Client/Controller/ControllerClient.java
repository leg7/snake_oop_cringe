package Client.Controller;

import Client.View.ViewSnakeGame;
import Client.View.PanelSnakeGame;
import Server.Model.InputMap;
import Utils.Features;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
	private PropertyChangeSupport pcs;
	private Thread t;

	public ControllerClient() {
		gson = new Gson();
		pcs = new PropertyChangeSupport(this);
		var im = inputMap("layouts/arenaNoWall.lay");

		var p = new PanelSnakeGame(
				im.getSizeX(), im.getSizeY(),
				im.get_walls(),
				im.getStart_snakes(), im.getStart_items());
		viewSnakeGame = new ViewSnakeGame(this, p);
		viewSnakeGame.addPropertyChangeListener(this);
		t = null;
	}

	public void start(String serverIP, int port) throws IOException {
		socket = new Socket(serverIP, port);

		DataInputStream soIn = new DataInputStream(socket.getInputStream());
		PrintWriter soOut = new PrintWriter(socket.getOutputStream(), true);

		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msg;
					while ((msg = soIn.readUTF()) != null && !Thread.interrupted()) {
						System.out.println(msg);
						pcs.firePropertyChange("features", null,
								gson.fromJson(msg, Features.class));
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

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
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
					break;
				case "running":
					if ((boolean) evt.getOldValue() && !(boolean) evt.getNewValue() && t != null) {
						t.interrupt();
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
