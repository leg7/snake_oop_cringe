package Server.Model.Agent;

import Server.Model.SnakeGame;
import Server.Model.Agent.*;
import Utils.*;

import java.util.ArrayList;

public class SnakeUserControlled extends Snake implements AgentUserControlled {
	private AgentKeybindings keybindings;

	public SnakeUserControlled(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsInvincible,
		int roundsSick,
		ColorSnake color,
		SnakeGame g,
		AgentKeybindings keys)
	{
		super(pos, action, roundsInvincible, roundsSick, color, g);
		keybindings = keys;
	}

	public AgentKeybindings keybindings() {
		return keybindings;
	}
}
