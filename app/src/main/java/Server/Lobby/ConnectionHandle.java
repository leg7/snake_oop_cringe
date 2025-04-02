package Server.Lobby;

import Utils.LobbyInfo;
import Utils.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConnectionHandle implements Runnable {
    static int lobbyId = 0;
    private HashMap<Integer, GameLobby> gameLobbies = new HashMap();
    private ServerSocket serveurSocket;

    public ConnectionHandle(ServerSocket serveurSocket) {
        this.serveurSocket = serveurSocket;
    }

    public void run() {
        try {
            BufferedReader in;
            String ch; // la chaine recue
            LobbyInfo lobbyInfo = null;
            Gson gson = new Gson();

            while (true) {
                Socket clientSocket = serveurSocket.accept();
                System.out.println("Connexion établie avec le client : " + clientSocket.getInetAddress());

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                if ((ch = in.readLine()) != null) {
                    // Recoit les infos du lobby du client,
                    // avec type MAP_INFO et data : LobbyInfo(mapPath, isAlone, isPVP).
                    Message message = gson.fromJson(ch, Message.class);
                    if (message.getType().equals(Message.Type.MAP_INFO)) {
                        lobbyInfo = gson.fromJson(message.getData(), LobbyInfo.class);
                        System.out.println(lobbyInfo);
                    }

                    if (lobbyInfo != null) {
                        GameLobby lobby = findAvailableLobby(lobbyInfo.mapPath(), lobbyInfo.isAlone(),
                                lobbyInfo.isPVP());
                        gameLobbies.put(lobbyId, lobby);
                        lobbyId++;
                        lobby.addClient(clientSocket);

                        if (lobby.isFull()) {
                            // Envoie le message du lancement de la partie au client,
                            // avec type GAME_START et data : gameStarted(true).
                            boolean gameStarted = true;
                            String json = gson.toJson(gameStarted);
                            String msg = Message.makeMessage(Message.Type.GAME_START, json);
                            System.out.println(msg);

                            System.out.println("Lobby " + lobby.getId() + " starts ");
                            System.out.println("Client(s) in lobby : ");

                            for (Socket client : lobby.getClients()) {
                                System.out.println("Client " + client);
                                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                                out.writeUTF(msg);
                            }

                            lobby.startGame();
                        }
                    } else {
                        System.err.println("Erreur lobby info du client : " + clientSocket.getInetAddress());
                    }
                }

                clearEmptyLobbies();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameLobby createLobby(String mapPath, boolean isAloneMap, boolean isPVP) {
        GameLobby lobby = new GameLobby(mapPath, isAloneMap, isPVP);
        gameLobbies.put(lobby.getId(), lobby);
        return lobby;
    }

    public GameLobby getLobby(int id) {
        return gameLobbies.get(id);
    }

    public void removeLobby(int id) {
        gameLobbies.remove(id);
    }

    public GameLobby findAvailableLobby(String mapPath, boolean isAloneMap, boolean isPVP) {
        // Exclut les lobbies solo
        if (isAloneMap) {
            return createLobby(mapPath, true, false);
        }

        // Exclut les lobbies PVE
        if (!isPVP) {
            return createLobby(mapPath, false, false);
        }

        // Trouver un lobby PVP disponible sinon en créer un
        for (GameLobby lobby : gameLobbies.values()) {
            if (!lobby.isFull() && lobby.getMap().equals(mapPath)) {
                return lobby;
            }
        }
        return createLobby(mapPath, false, true);
    }

    public void clearEmptyLobbies() {
        for (GameLobby lobby : gameLobbies.values()) {
            if (lobby.isEmpty()) {
                removeLobby(lobby.getId());
            }
        }
    }

}
