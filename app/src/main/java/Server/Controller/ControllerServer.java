package Server.Controller;

import Server.Model.*;
import Server.Model.Agent.*;
import Utils.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.beans.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Client.View.PanelSelection.MapOptions;

// recoit les commandes, maj le game et renvoie l'état du jeu.
public class ControllerServer implements Runnable, PropertyChangeListener {
	SnakeGame game;
	Socket socket;
	Vector<Socket> clients;
	HashMap<Socket, AgentUserControlled> clientToAgent;
	Gson gson;

	public ControllerServer(SnakeGame game, Socket socket, Vector<Socket> clients) {
		super();
		this.game = game;
		this.socket = socket;
		this.clients = clients;
		this.clientToAgent = new HashMap<>();
		this.gson = new Gson();

		ArrayList<Agent> agentsList = game.getAgents();
		for (int i = 0; i < agentsList.size(); i++) {
			if (agentsList.get(i) instanceof AgentUserControlled) {
				AgentUserControlled a = (AgentUserControlled) agentsList.get(i);
				if (!clientToAgent.containsValue(a)) {
					clientToAgent.put(this.clients.get(i), a);
				}
			}
		}

		// S'inscrire comme listener du modèle SnakeGame
		game.addPropertyChangeListener("update", this);
		game.addPropertyChangeListener("gameOverForThisSnake", this);
	}

	public void run() {
		try {
			connexionLog();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String ch; // la chaine recue

			// Recoit les input key du client,
			// avec type ACTION et data : AgentAction.
			while ((ch = in.readLine()) != null && !game.gameOver()) {
				Message message = gson.fromJson(ch, Message.class);
				if (message.getType() == Message.Type.ACTION) {
					AgentAction action = null;
					action = gson.fromJson(message.getData(), AgentAction.class);
					System.out.println(action);
					clientMoveLog(socket, action.name());
					handleCommand(clientToAgent.get(socket), action);
				} else if (message.getType() == Message.Type.QUIT) {
					// TODO: Remove agent and stop listening
				}
			}

			deconnexionLog();

			clientToAgent.remove(socket);

			socket.close();

			// Lobby vide, on arrête le jeu
			if (clients.isEmpty() && game != null) {
				game.removePropertyChangeListener(this);
			}

		} catch (IOException e) {
			System.err.println("Erreur avec le client : " + clients.indexOf(socket) + " - "
					+ socket.getInetAddress() + "\t" + e);
		}
	}

	private void handleCommand(AgentUserControlled a, AgentAction action) {
		if (action != null)
			game.setAgentAction(a, action);
	}

	public void propertyChange(PropertyChangeEvent e) {
		Object obj = e.getNewValue();
		switch (e.getPropertyName()) {
			case "update":
				// System.out.println("update features : ");
				if (obj instanceof Features(var fss, var fis)) {
					Features features = (Features) obj;
					sendGameState(features);
				}
				break;
			case "gameOverForThisSnake":
				if (obj instanceof AgentUserControlled) {
					Agent a = (Agent) obj;
					System.out.println("Game Over pour le client : " + a);
					Socket client = findSocketWithAgent((AgentUserControlled) a);
					if (client != null) {
						clientToAgent.remove(client);
						clients.remove(client);
						sendGameOver(client);
						try {
							client.close();
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
				break;

			default:
				System.err.println("Erreur propertyChange : " + e.getPropertyName());
		}
	}

	private void sendGameState(Features features) {
		try {
			// Envoie le message d'actualisation de la partie du jeu au client,
			// avec type GAME_STATE et data : features.
			String json = gson.toJson(features);
			String msg = Message.makeMessage(Message.Type.GAME_STATE, json);
			System.out.println(msg);

			// Envoyer à tous les clients connectés
			for (Socket client : clients) {
				try {
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					out.writeUTF(msg);
					out.flush();
				} catch (IOException ex) {
					System.err.println("Erreur lors de l'envoi au client: " + ex.getMessage());
				}
			}
		} catch (Exception ex) {
			System.err.println("Erreur lors de la sérialisation/envoi: " + ex.getMessage());
		}
	}

	private void sendGameOver(Socket client) {
		Boolean gameOver = true;
		String json = gson.toJson(gameOver);
		String msg = Message.makeMessage(Message.Type.GAME_OVER, json);
		System.out.println(msg + " - Client : " + clients.indexOf(client) + " - " + client.getInetAddress());
		try {
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(msg);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Erreur lors de l'envoi au client: " + ex.getMessage());
		}
	}

	private void connexionLog() {
		System.out.println("Connexion établie avec le client : " + clients.indexOf(socket) + " - "
				+ socket.getInetAddress());
	}

	private void deconnexionLog() {
		System.out.println("Connexion fermé avec le client : " + clients.indexOf(socket) + " - "
				+ socket.getInetAddress());
	}

	private void clientMoveLog(Socket client, String direction) {
		System.out.println("Client : " + clients.indexOf(socket) + " - Move : " + direction);
	}

	private Socket findSocketWithAgent(AgentUserControlled a) {
		for (Socket client : clients) {
			if (clientToAgent.get(client) == a) {
				return client;
			}
		}
		return null;
	}

}
