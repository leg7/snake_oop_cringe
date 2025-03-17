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
import com.google.gson.Gson;


// Handle du serveur :
// recoit les commandes, maj le game et renvoie l'état du jeu.
public class ControllerServer implements Runnable {
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
			AgentUserControlled a = (AgentUserControlled) agentsList.get(i);
			if (a != null) {
				clientToAgent.put(socket, a);
			}
		}

		game.launch();
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

			 while ((ch = in.readLine()) != null) {

				for (Socket client : clients) {
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					clientMoveLog(socket, ch);
					handleCommand(clientToAgent.get(socket), ch);
					sendGameState();
				}

			}

			deconnexionLog();
			clients.remove(socket);

			socket.close();
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

	private void sendGameState() {
		Gson gson = new Gson();
		String json = gson.toJson(game.getAgents());
		json += gson.toJson(game.getItems());
		System.out.println(json);
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
