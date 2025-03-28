package Utils;

public class Message {

	public enum Type {
		// Server to Client
		GAME_START,
		GAME_OVER,
		GAME_STATE,

		// Cient to Server
		ACTION,
		MAP_INFO,
	}

	public static String makeMessage(Type type, String data) {
		return "{\"type\": \"" + type.toString() + "\", \"data\": \"" + data + "\" }";
	}
}
