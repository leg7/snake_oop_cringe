package leonardgomez.Model.Agent;

import leonardgomez.Model.SnakeGame;
import leonardgomez.Model.Agent.*;
import utils.*;

import java.util.ArrayList;

public class Snake implements Agent {
	private ArrayList<Position> pos;
	private AgentAction action = AgentAction.MOVE_UP;
	private boolean invincible;
	private boolean sick;
	private ColorSnake color;

	private SnakeGame game;
	private ActionStrategy strategy = new ActionStrategySurvivalNaive();
	private boolean grow = false;

	public Snake(
		ArrayList<Position> pos,
		AgentAction action,
		boolean invincible,
		boolean sick,
		ColorSnake color,
		SnakeGame g)
	{
		this.pos = new ArrayList<Position>(pos);
		this.action = action;
		this.invincible = invincible;
		this.sick = sick;
		this.color = color;
		game = g;
	}

	private int wrap(int x, int upperBound) {
		if (x < 0) {
			return upperBound + x;
		} else if (x >= upperBound) {
			return x % upperBound;
		} else {
			return x;
		}
	}

	public Position nextPosition(AgentAction candidateAction) {
		final var head = pos.getFirst();

		var x = head.x();
		var y = head.y();
		switch (candidateAction) {
			case AgentAction.MOVE_UP: y -= 1; break;
			case AgentAction.MOVE_DOWN: y += 1; break;
			case AgentAction.MOVE_LEFT: x -= 1; break;
			case AgentAction.MOVE_RIGHT: x += 1; break;
		};

		final var X = game.inputMap().getSizeX();
		final var Y = game.inputMap().getSizeY();
		x = wrap(x, X);
		y = wrap(y, Y);

		return new Position(x, y);
	}

	@Override
	public void move() {
		strategy.setNewAction(this);
		pos.addFirst(nextPosition(this.action));

		grow();

		if (!grow) {
			pos.removeLast();
		} else {
			grow = false;
		}
	}

	public void grow() {
		grow = true;
	}

	public boolean legalMove(AgentAction newAction) {
		if (pos.size() <= 1) {
			return true;
		} else {
			return !AgentAction.areOpposite(action, newAction);
		}
	}

	@Override
	// Passed action should be legal
	public boolean setAction(AgentAction a) {
		if (legalMove(a)) {
			action = a;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ArrayList<Position> getPositions() { return pos; }
	public AgentAction action() { return action; }
	public boolean invincible() { return invincible; }
	public boolean sick() { return sick; }
	public ColorSnake color() { return color; }
}
