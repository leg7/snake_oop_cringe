package Server.Model;

import Server.Model.Agent.*;
import Server.Model.Item.*;

import Utils.*;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.*;
import java.beans.*;

public abstract class SnakeGame implements Runnable {
	protected Thread thread;
	protected int turn, turnMax;
	protected boolean isRunning;
	protected long sleepDelay;
	protected final PropertyChangeSupport pcs;

	protected InputMap inputMap;
	protected ArrayList<Agent> agents = new ArrayList<Agent>();
	protected ArrayList<Item> items = new ArrayList<Item>();

	protected final static Random random = new Random();
	protected final static int pItemSpawn = 100; // Je regle la proba a 100% pareque c'est plus interesant
	protected final static int pItemRange = 101;

	protected final static int snakeSickRounds = 20;
	protected final static int snakeInvincibleRounds = 20;


	public SnakeGame(int turnMax, long sleepDelay, InputMap inputMap) {
		this.turnMax = turnMax;
		this.sleepDelay = sleepDelay;
		pcs = new PropertyChangeSupport(this);
		this.inputMap = inputMap;

		reinitialize();
	}

	public void reinitialize() {
		//pcs.firePropertyChange("turn", turn, 0);

		turn = 0;
		isRunning = false;

		items = inputMap.getStart_items().stream()
			.map(fi -> new Item(new Position(fi.getX(), fi.getY()), fi.getItemType()))
			.collect(Collectors.toCollection(ArrayList::new));

		updateView();
	}


	private void updateView() {
		// final var isInvincible = false;
		// final var isSick = false;

		final var featuresSnake = agents.stream()
			.map(a -> new FeaturesSnake(a.getPositions(), a.action(), a.color(), a.invincible(), a.sick()))
			.collect(Collectors.toCollection(ArrayList::new));

		final var featuresItem = items.stream()
			.map(i -> new FeaturesItem(i.position().x(), i.position().y(), i.type()))
			.collect(Collectors.toCollection(ArrayList::new));

		final var features = new Features(featuresSnake, featuresItem);
		pcs.firePropertyChange("update", null, features);
	}


	public boolean legalMove(Agent agent, AgentAction candidateAction) {
		final var pos = agent.nextPosition(candidateAction);
		final var walls = inputMap.get_walls();
		final boolean posIsNotAWall = !walls[pos.x()][pos.y()];
		return posIsNotAWall;
	}

	public void run() {
		boolean continueGame = agents.size() != 0;
		while (continueGame && turn < turnMax && isRunning) {
			//pcs.firePropertyChange("turn", turn, ++turn);
			++turn;
			var agentsToRemove = new HashSet<Agent>();

			for (var a : agents) {
				a.move();
				var pos = a.getPositions();
				var head = pos.getFirst();

				// Check if the agent's head ran into an item
				if (!a.sick()) {
					var originalSize = items.size(); // Used later to check if the agent got the item

					items.removeIf(i -> {
						if (i.position().equals(head)) {
							switch (i.type()) {
							case ItemType.APPLE:
								a.grow();
								return true;
							case ItemType.BOX:
								return true;
							case ItemType.SICK_BALL:
								a.makeSick(snakeSickRounds);
								return true;
							case ItemType.INVINCIBILITY_BALL:
								a.makeInvincible(snakeInvincibleRounds);
								return true;
							default:
								return false;
							}
						}
						return false;
					});

					boolean itemAcquired = originalSize != items.size();
					if (itemAcquired) {
						var roll = random.nextInt(pItemRange);
						boolean itemSpawns = roll < pItemSpawn;
						if (itemSpawns) {
							var types = ItemType.values();
							var typeIndex = random.nextInt(types.length);
							System.out.println(typeIndex);
							var itemType = types[typeIndex];

							var itemPosX = random.nextInt(inputMap().getSizeX());
							var itemPosY = random.nextInt(inputMap().getSizeY());
							var itemPos = new Position(itemPosX, itemPosY);

							var item = new Item(itemPos, itemType);
							items.add(item);
						}
					}
				}

				// Check if an agent ran into himself
				var uniquePos = new HashSet(pos);
				boolean hasDuplicates = uniquePos.size() != pos.size();
				if (hasDuplicates) {
					agentsToRemove.add(a);
					continue;
				}

				// Check if an agent ran into a wall
				var walls = inputMap().get_walls();
				if (walls[head.x()][head.y()]) {
					agentsToRemove.add(a);
					continue;
				}
			}

			// Check if an agent ran into another agent
			for (int i = 0; i < agents.size() - 1; ++i) {
				var a = agents.get(i);
				var aPos = a.getPositions();
				var aHead = aPos.getFirst();
				var aTail = aPos.subList(1, aPos.size());

				for (int j = i + 1; j < agents.size(); ++j) {
					var b = agents.get(j);
					var bPos = b.getPositions();
					var bHead = bPos.getFirst();
					var bTail = bPos.subList(1, bPos.size());

	/* Contradiction dans les regles du jeux alors j'enleve ce cas qui pose probleme
	 *
	 * Si la tête d’un agent snake se retrouve sur la position d’un autre snake (tête ou corps) et
	 * que la taille de son corps est supérieure ou égale à celle de l’autre snake, il le mange et le fait
	 * disparaître.
	 * (Ce cas inclus aussi les cas suivant par les conditions tete et sup ou egale, d'ou la contradiction.)
	 *
	 * Il peut arriver que deux têtes de snake de même taille se rencontre sur la même case. Dans
	 * ce cas, les deux snakes sont simultanément éliminés.
	*/
					// boolean headbutt = aHead == bHead;
					// boolean equallyStrong = aPos.size() == bPos.size();
					boolean hit = bPos.contains(aHead) || aPos.contains(bHead);
					boolean aStronger = aPos.size() >= bPos.size();
					boolean aWeaker = aPos.size() < bPos.size();

					if (hit && aStronger) {
						agentsToRemove.add(b);
					} else if (hit && aWeaker) {
						agentsToRemove.add(a);
					}
				}
			}

			agentsToRemove.forEach(a -> agents.remove(a));

			updateView();
			System.out.println("Update view - tour : " + turn);

			try {
				Thread.sleep(sleepDelay);
			} catch (Exception e) {}
		}

		System.out.println("Game Over");
	}

	public void launch() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	// Property method
	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(property, listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(property, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}


	// Getters
	public InputMap inputMap() { return inputMap; }

	public int getTurn() { return turn; }

	public ArrayList<Agent> getAgents() { return agents; }

	public ArrayList<Item> getItems() { return items; }

	// Setters
	public void setSleepDelay(long speed) { sleepDelay = speed; }

	public void setAgentAction(Agent agent, AgentAction action) { agent.setAction(action); }

	public void pause() { isRunning = false; }
}
