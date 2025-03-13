package Server.Model;

import Server.Model.Agent.*;
import Utils.*;

import java.util.ArrayList;
import java.util.stream.*;

public class SnakeGameAI extends SnakeGame {
	public SnakeGameAI(int turnMax, long sleepDelay, InputMap inputMap) {
		super(turnMax, sleepDelay, inputMap);
	}

	@Override
	protected void initializeGame() {
		agents = inputMap.getStart_snakes().stream()
			.map(fs -> AgentFabric.snakeAISurvivalNaive(
					new ArrayList<Position>(fs.getPositions()),
					fs.getLastAction(),
					fs.isInvincible() ? snakeInvincibleRounds : 0,
					fs.isSick() ? snakeSickRounds : 0,
					fs.getColorSnake(),
					this)
			)
			.collect(Collectors.toCollection(ArrayList::new));

		super.initializeGame();
	}
}
