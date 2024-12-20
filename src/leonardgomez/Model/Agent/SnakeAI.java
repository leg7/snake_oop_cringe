package leonardgomez.Model.Agent;

import leonardgomez.Model.SnakeGame;
import leonardgomez.Model.Agent.*;
import utils.*;

import java.util.ArrayList;

public class SnakeAI extends Snake {
	private ActionStrategy strategy;

	public SnakeAI(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsInvincible,
		int roundsSick,
		ColorSnake color,
		SnakeGame g,
		ActionStrategy s)
	{
		super(pos, action, roundsInvincible, roundsSick, color, g);
		strategy = s;
	}

	@Override
	public void move() {
		strategy.setNewAction(this);
		super.move();
	}
}
