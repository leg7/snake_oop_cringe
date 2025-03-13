package Server.Model.Agent;

import leonardgomez.Model.Agent.*;
import utils.*;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class ActionStrategySurvivalNaive implements ActionStrategy {
	final static Random random = new Random();

	public void setNewAction(Agent a) {
		AgentAction randomAction = null;
		boolean suicide = true;
		var actions = new ArrayList<AgentAction>(
			Arrays.asList(AgentAction.values())
		);

		do {
			int randomIndex = random.nextInt(actions.size());
			randomAction = actions.get(randomIndex);
			actions.remove(randomIndex);

			var nextPos = a.nextPosition(randomAction);
			suicide = a.getPositions().contains(nextPos);
		} while (actions.size() > 0 && (suicide || a.setAction(randomAction) == false));
	}
}
