package leonardgomez.Model.Agent;

import leonardgomez.Model.Agent.*;
import leonardgomez.Model.SnakeGame;
import utils.*;

import java.util.ArrayList;
import java.awt.event.KeyEvent;

public class AgentFabric {
	private AgentFabric() { super(); }

	public static Agent snakeAI(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g,
		ActionStrategy s)
	{
		return new SnakeAI(pos, action, roundsToBeInvincible, roundsToBeSick, color, g, s);
	}

	public static Agent snakeAIRandom(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g)
	{
		return new SnakeAI(pos, action, roundsToBeInvincible, roundsToBeSick, color, g, new ActionStrategyRandom());
	}

	public static Agent snakeAISurvivalNaive(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g)
	{
		return new SnakeAI(pos, action, roundsToBeInvincible, roundsToBeSick, color, g, new ActionStrategySurvivalNaive());
	}

	final static AgentKeybindings zqsd = new AgentKeybindings(KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_D);
	final static AgentKeybindings wasd = new AgentKeybindings(KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_D);
	final static AgentKeybindings pad = new AgentKeybindings(KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT);

	public static Agent snakeUserControlledZQSD(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g)
	{
		return new SnakeUserControlled(pos, action, roundsToBeInvincible, roundsToBeSick, color, g, zqsd);
	}

	public static Agent snakeUserControlledWASD(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g)
	{
		return new SnakeUserControlled(pos, action, roundsToBeInvincible, roundsToBeSick, color, g, wasd);
	}

	public static Agent snakeUserControlledPad(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsToBeInvincible,
		int roundsToBeSick,
		ColorSnake color,
		SnakeGame g)
	{
		return new SnakeUserControlled(pos, action, roundsToBeInvincible, roundsToBeSick, color, g, pad);
	}
}
