package Server.Model.Agent;

import Server.Model.SnakeGame;
import Server.Model.Agent.*;
import Utils.*;

import java.util.ArrayList;

public abstract class Snake implements Agent {
	private ArrayList<Position> pos;
	private AgentAction action = AgentAction.MOVE_UP;
	private SnakeGame game;

	private int roundsToBeInvincible = 0;
	private int roundsToBeSick = 0;

	private ColorSnake color;

	private boolean grow = false;

	public Snake(
		ArrayList<Position> pos,
		AgentAction action,
		int roundsInvincible,
		int roundsSick,
		ColorSnake color,
		SnakeGame g)
	{
		super();
		this.pos = new ArrayList<Position>(pos);
		this.action = action;
		roundsToBeSick = roundsSick;
		roundsToBeInvincible = roundsInvincible;
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
		pos.addFirst(nextPosition(this.action));

		if (!grow) {
			pos.removeLast();
		} else {
			grow = false;
		}

		if (roundsToBeSick > 0) {
			roundsToBeSick--;
		}
		if (roundsToBeInvincible > 0) {
			roundsToBeInvincible--;
		}
	}

	public void makeSick(int roundsToBeSick) {
		this.roundsToBeSick = roundsToBeSick;
	}

	public void makeInvincible(int roundsToBeInvincible) {
		this.roundsToBeInvincible = roundsToBeInvincible;
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
	public boolean invincible() { return roundsToBeInvincible > 0; }
	public boolean sick() { return roundsToBeSick > 0; }
	public ColorSnake color() { return color; }
}
