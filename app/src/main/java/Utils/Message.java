package Utils;

import com.google.gson.Gson;

public class Message {

	public enum Type {
		// Server to Client
		GAME_START,
		GAME_OVER,
		GAME_STATE,

		// Cient to Server
		ACTION,
		MAP_INFO,
		QUIT,
	}

	private Type type;
	private String data;

	public Message(Type type, String data) {
		this.type = type;
		this.data = data;
	}

	public Type getType() {
		return type;
	}

	public String getData() {
		return data;
	}

	public static String makeMessage(Type type, String data) {
		Gson gson = new Gson();
		return gson.toJson(new Message(type, data));
	}
}
