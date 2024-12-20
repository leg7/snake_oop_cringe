package leonardgomez.Model.Agent;

import leonardgomez.Model.Agent.*;
import leonardgomez.Model.SnakeGame;

import java.util.ArrayList;
import utils.*;

public class AgentFabric {
	private AgentFabric() { super(); }

	public static Agent snake(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g)
	{
		return new Snake(pos, action, roundsToBeInvincible, roundsToBeSick, color, g);
	}
}
