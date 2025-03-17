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


// Handle du serveur :
// recoit les commandes, maj le game et renvoie l'état du jeu.
public class ControllerServer implements Runnable, PropertyChangeListener {
	SnakeGame game;
    	private Socket socket;
    	Vector<Socket> clients = new Vector<>();
	HashMap<Socket, AgentUserControlled> clientToAgent = new HashMap<>();

	public ControllerServer (SnakeGame game, Socket socket, Vector<Socket> clients) {
		super();
		this.game = game;
		this.socket = socket;
		this.clients = clients;

		ArrayList<Agent> agentsList = game.getAgents();
		for (int i = 0; i < agentsList.size(); i++) {
			if (agentsList.get(i) instanceof AgentUserControlled) {
				AgentUserControlled a = (AgentUserControlled) agentsList.get(i);
				if (!clientToAgent.containsValue(a)) {
					clientToAgent.put(socket, a);
					break;
				}
			}
		}

		// S'inscrire comme listener du modèle SnakeGame
		game.addPropertyChangeListener("update", this);
	}

	public ControllerServer (Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		try {
			connexionLog();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String ch;  // la chaine recue
			Gson gson = new Gson();

			 while ((ch = in.readLine()) != null) {
				ch = gson.fromJson(ch, String.class);

				for (Socket client : clients) {
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					clientMoveLog(socket, ch);
					handleCommand(clientToAgent.get(socket), ch);
				}
			}

			deconnexionLog();
			clients.remove(socket);
			clientToAgent.remove(socket);

			socket.close();

			// Lobby vide, on arrête le jeu
			if (clients.isEmpty() && game != null) {
				game.removePropertyChangeListener(this);
			}

			System.out.println("server close");
		} catch (IOException e) {
			System.err.println("Erreur avec le client : " + clients.indexOf(socket) + " - " + socket.getInetAddress() + "\t" + e);
		}
	}

	private void handleCommand(AgentUserControlled a, String ch) {
		switch (ch) {
			case "UP":
				game.setAgentAction(a, AgentAction.MOVE_UP);
				break;
			case "DOWN":
				game.setAgentAction(a, AgentAction.MOVE_DOWN);
				break;
			case "LEFT":
				game.setAgentAction(a, AgentAction.MOVE_LEFT);
				break;
			case "RIGHT":
				game.setAgentAction(a, AgentAction.MOVE_RIGHT);
				break;
			default:
				break;
		}
	}

	public void propertyChange(PropertyChangeEvent e) {
		switch (e.getPropertyName()) {
			case "update":
				var obj = e.getNewValue();
				if (obj instanceof Features(var fss, var fis)) {
				 	Features features = (Features) obj;
					sendGameState(features);
				} else {
					System.exit(69);
				}
			break;

			default:
				System.exit(69);
		}
	}

	private void sendGameState(Features features) {
		try {
			Gson gson = new Gson();
			String json = gson.toJson(features);

			// Envoyer à tous les clients connectés
			for (Socket client : clients) {
				try {
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					out.writeUTF(json);
					out.flush();
				} catch (IOException ex) {
					System.err.println("Erreur lors de l'envoi au client: " + ex.getMessage());
				}
			}
		} catch (Exception ex) {
			System.err.println("Erreur lors de la sérialisation/envoi: " + ex.getMessage());
		}
	}

	private void connexionLog() {
		System.out.println("Connexion établie avec le client : " + clients.indexOf(socket) + " - " + socket.getInetAddress());
	}

	private void deconnexionLog() {
		System.out.println("Connexion fermé avec le client : " + clients.indexOf(socket) + " - " + socket.getInetAddress());
	}

	private void clientMoveLog(Socket client, String direction) {
		System.out.println("Client : " + clients.indexOf(socket) + " - Move : " + direction);
	}

}
