package Server.Model.Agent;

import java.util.ArrayList;
import utils.*;

public interface Agent {
	ArrayList<Position> getPositions();
	AgentAction action();
	boolean invincible();
	boolean sick();
	ColorSnake color();

	boolean setAction(AgentAction a);
	public void grow();
	public void makeSick(int roundsToBeSick);
	public void makeInvincible(int roundsToBeInvincible);

	boolean legalMove(AgentAction newAction);

	void move();
	Position nextPosition(AgentAction candidateAction);
}
