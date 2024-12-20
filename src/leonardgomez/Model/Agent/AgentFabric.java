package leonardgomez.Model.Agent;

import leonardgomez.Model.Agent.*;
import leonardgomez.Model.SnakeGame;

import java.util.ArrayList;
import utils.*;

public class AgentFabric {
	private AgentFabric() {}

	public static Agent snake(
		ArrayList<Position> pos,
		AgentAction action,
		boolean invincible,
		boolean sick,
		ColorSnake color,
		SnakeGame g)
	{
		return new Snake(pos, action, invincible, sick, color, g);
	}
}
