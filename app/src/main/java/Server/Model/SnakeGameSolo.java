package Server.Model;

import Server.Model.Agent.*;
import Utils.*;

import java.util.ArrayList;

public class SnakeGameSolo extends SnakeGame {
	public SnakeGameSolo(int turnMax, long sleepDelay, InputMap inputMap) {
		super(turnMax, sleepDelay, inputMap);
	}


	@Override
	protected void initializeGame() {
		var snakes = inputMap.getStart_snakes();

		var s1 = snakes.get(0);
		var s2 = snakes.get(1);
		var a1 = (AgentUserControlled)AgentFabric.snakeUserControlledZQSD(
					new ArrayList<Position>(s1.getPositions()),
					s1.getLastAction(),
					s1.isInvincible() ? snakeInvincibleRounds : 0,
					s1.isSick() ? snakeSickRounds : 0,
					s1.getColorSnake(),
					this);

		agents = new ArrayList<Agent>();
		agents.add(a1);

		super.initializeGame();
	}


}
