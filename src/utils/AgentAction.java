package utils;

public enum AgentAction {
	MOVE_UP,
	MOVE_DOWN,
	MOVE_LEFT,
	MOVE_RIGHT;

	private static AgentAction[] opposite = { MOVE_DOWN, MOVE_UP, MOVE_RIGHT, MOVE_LEFT };
	public static boolean areOpposite(AgentAction a, AgentAction b) {
		return opposite[a.ordinal()] == b;
	}
}
