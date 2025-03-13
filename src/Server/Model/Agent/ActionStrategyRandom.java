package Server.Model.Agent;

import leonardgomez.Model.Agent.*;
import utils.*;

import java.util.Random;

public class ActionStrategyRandom implements ActionStrategy {
	final static AgentAction[] actions = AgentAction.values();
	final static Random random = new Random();

	public void setNewAction(Agent a) {
		AgentAction randomAction = null;
		do {
			int randomIndex = random.nextInt(actions.length);
			randomAction = actions[randomIndex];
		} while (!a.legalMove(randomAction));

		a.setAction(randomAction);
	}
}
