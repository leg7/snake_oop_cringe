package Server.Lobby;

import Server.Controller.ControllerServer;
import Server.Model.InputMap;
import Server.Model.*;

import java.net.Socket;
import java.util.Vector;

public class GameLobby {
    static int id;
    private int maxPlayers;
    private Vector<Socket> clients = new Vector<>();
    private String mapPath;
    private InputMap inputMap;
    private SnakeGame snakeGame;
    private boolean isPVP = false;

    GameLobby(String mapPath, boolean isAloneMap, boolean isPVP) {
        this.mapPath = mapPath;
        this.inputMap = inputMap(mapPath);

        if (isAloneMap) {
            this.maxPlayers = 1;
            this.snakeGame = new SnakeGameSolo(1000, 100, inputMap);
        } else {
            if (isPVP) {
                this.maxPlayers = 2;
                this.snakeGame = new SnakeGamePVP(1000, 100, inputMap);
                this.isPVP = true;
            } else {
                this.maxPlayers = 1;
                this.snakeGame = new SnakeGamePVE(1000, 100, inputMap);
            }
        }

        ++id;
    }

    public boolean addClient(Socket socket) {
        if (isFull()) {
            return false;
        }

        clients.add(socket);
        return true;
    }

    public void startGame() {
        snakeGame.launch();
        for (Socket client : clients) {
            ControllerServer controllerServer = new ControllerServer(snakeGame, client, clients);
            Thread thread = new Thread(controllerServer);
            thread.start();
        }
    }

    boolean isFull() {
        return clients.size() >= maxPlayers;
    }

    boolean isEmpty() {
        return clients.isEmpty();
    }
    public int getId() {
        return id;
    }

    public String getMap() {
        return mapPath;
    }

    public InputMap getInputMap() {
        return inputMap;
    }

    public boolean isAloneMode() {
        return maxPlayers == 1;
    }

    public boolean isPVPMode() {
        return maxPlayers == 2 && isPVP;
    }

    public Vector<Socket> getClients() {
        return clients;
    }

    public SnakeGame getSnakeGame() {
        return snakeGame;
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
