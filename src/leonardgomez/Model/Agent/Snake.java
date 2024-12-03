package leonardgomez.Model.Agent;
import java.util.ArrayList;
import utils.Position;
import utils.AgentAction;
import java.beans.*;

class Snake {
	private ArrayList<Position> pos;
	private AgentAction action = AgentAction.MOVE_UP;
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public Snake(Position initial) {
		pos.add(initial);
	}

	public Snake(ArrayList<Position> pos) {
		this.pos = pos;
	}

	public void move() {
		grow();
		pos.removeFirst();
	}

	public void grow() {
		var oldPos = pos;

		var head = pos.getLast();
		var x = 0;
		var y = 0;
		switch (action) {
			case AgentAction.MOVE_UP:    y = -1; break;
			case AgentAction.MOVE_DOWN:  y = 1;  break;
			case AgentAction.MOVE_LEFT:  x = -1; break;
			case AgentAction.MOVE_RIGHT: x = 1;  break;
		}
		Position n = new Position(head.getX() + x, head.getY() + y);
		pos.add(n);

		// I think oldPos won't be a copy
		pcs.firePropertyChange("positions", oldPos, pos);
	}

	public void setAction(AgentAction a) {
		pcs.firePropertyChange("action", action, a);
		action = a;
	}

	public AgentAction getAction() {
		return action;
	}

	public ArrayList<Position> getPositions() {
		return pos;
	}

	public void addPropertyChangeListener(PropertyChangeListener pl) {
		this.pcs.addPropertyChangeListener(pl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pl) {
		this.pcs.removePropertyChangeListener(pl);
	}
}
