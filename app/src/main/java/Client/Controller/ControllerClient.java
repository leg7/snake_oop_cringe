package Client.Controller;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.Gson;
import Client.View.PanelSelection;
import Client.View.PanelSnakeGame;
import Client.View.ViewSnakeGame;
import Client.View.PanelSelection.MapOptions;
import Server.Model.InputMap;
import Utils.AgentAction;
import Utils.Features;
import Utils.LobbyInfo;
import Utils.Message;

public class ControllerClient implements PropertyChangeListener {
	private Gson gson;
	private PropertyChangeSupport pcs;
	private ViewSnakeGame viewSnakeGame;
	private boolean running;
	private InputMap inputMap;

	private Socket socket;
	private DataInputStream socketIn;
	private PrintWriter socketOut;

	private static int CELL_SIZE = 60;

	public ControllerClient(String serverIP, int port) throws IOException {
		this.gson = new Gson();
		this.pcs = new PropertyChangeSupport(this);
		this.viewSnakeGame = new ViewSnakeGame(this);
		this.running = false;

		this.socket = new Socket(serverIP, port);
		this.socketIn = new DataInputStream(socket.getInputStream());
		this.socketOut = new PrintWriter(socket.getOutputStream(), true);

		this.viewSnakeGame.addPropertyChangeListener(this);
	}

	public void listen() throws IOException {
		running = true;
		String msg;
		while (running && (msg = socketIn.readUTF()) != null) {
			System.out.println(msg);
			Message message = gson.fromJson(msg, Message.class);
			switch (message.getType()) {
				case GAME_START:
					System.out.println("GAME_START received");
					if (inputMap != null) {
						System.out.println("inputMap ok");
						viewSnakeGame.setPanel(new PanelSnakeGame(inputMap.getSizeX(),
								inputMap.getSizeY(), inputMap.get_walls(),
								inputMap.getStart_snakes(),
								inputMap.getStart_items()),
								new Dimension(
										inputMap.getSizeX() * CELL_SIZE,
										inputMap.getSizeY() * CELL_SIZE));
					}
					break;
				case Message.Type.GAME_STATE:
					Features features = gson.fromJson(message.getData(), Features.class);
					pcs.firePropertyChange("features", null, features);
					break;
				case GAME_OVER:
					if (viewSnakeGame.getPanel() instanceof PanelSnakeGame) {
						JPanel panel = new JPanel();
						panel.add(new JLabel("<html><h1>Game Over</h1><html>"));
						viewSnakeGame.setPanel(panel);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						viewSnakeGame.setPanel(new PanelSelection(), new Dimension(500, 300));
					}
					break;
				default:
					break;

			}
		}
		socket.close();
		running = false;
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
		switch (evt.getPropertyName()) {
			case "action":
				if (!socket.isClosed()) {
					AgentAction action = (AgentAction) evt.getNewValue();
					socketOut.println(Message.makeMessage(Message.Type.ACTION, gson.toJson(action)));
				}
				break;
			case "running":
				if ((boolean) evt.getOldValue() && !(boolean) evt.getNewValue()) {
					running = false;
				}
				socketOut.println(Message.makeMessage(Message.Type.QUIT, gson.toJson(null)));
				break;
			case "mapSelected":
				if (!socket.isClosed()) {
					LobbyInfo info = optionsToInfo((MapOptions) evt.getNewValue());
					try {
						inputMap = new InputMap(info.mapPath());
						socketOut.println(Message.makeMessage(Message.Type.MAP_INFO,
								gson.toJson(info)));
						JPanel panel = new JPanel();
						panel.setLayout(new GridBagLayout());
						panel.add(new JLabel("Waiting for server..."));
						viewSnakeGame.setPanel(panel, null);
					} catch (Exception e) {
						System.err.println("Client failed to open input map...");
						e.printStackTrace();
					}
				}
				break;
		}
	}

	private static LobbyInfo optionsToInfo(MapOptions options) {
		String mapPath = new String();
		if (options.mode() != PanelSelection.Mode.Solo) {
			mapPath = "arena";
		}
		if (options.size() == PanelSelection.Size.Small) {
			if (!mapPath.isEmpty()) {
				mapPath = mapPath.substring(0, 1).toUpperCase() + mapPath.substring(1);
			}
			mapPath = "small" + mapPath;
		}
		if (mapPath.isEmpty()) {
			mapPath = "alone";
		}
		if (!options.wall()) {
			mapPath += "NoWall";
		}
		mapPath = "layouts/" + mapPath + ".lay";
		boolean isAlone = options.mode() == PanelSelection.Mode.Solo;
		boolean isPVP = options.mode() == PanelSelection.Mode.PvP;
		return new LobbyInfo(mapPath, isAlone, isPVP);
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
