package Server.Model;

import Server.Model.Agent.*;
import Utils.*;

import java.util.ArrayList;

public class SnakeGameSolo extends SnakeGame {
	public SnakeGameSolo(int turnMax, long sleepDelay, InputMap inputMap) {
		super(turnMax, sleepDelay, inputMap);
	}

	@Override
	public void reinitialize() {
		var snakes = inputMap.getStart_snakes();

		assert snakes.size() == 1;
		var s1 = snakes.get(0);
		var a1 = (AgentUserControlled)AgentFabric.snakeUserControlledZQSD(
					new ArrayList<Position>(s1.getPositions()),
					s1.getLastAction(),
					s1.isInvincible() ? snakeInvincibleRounds : 0,
					s1.isSick() ? snakeSickRounds : 0,
					s1.getColorSnake(),
					this);

		agents.add(a1);

		super.reinitialize();
	}


}
