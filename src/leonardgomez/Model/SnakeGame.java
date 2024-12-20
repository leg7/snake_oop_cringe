package leonardgomez.Model;

import leonardgomez.View.ViewSnakeGame;
import leonardgomez.Model.Agent.*;
import leonardgomez.Model.Item.*;

import utils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.*;
import java.beans.*;

public class SnakeGame extends Game {
	private InputMap inputMap;
	private ArrayList<Agent> agents;
	private ArrayList<Item> items;

	public SnakeGame(int turnMax, long sleepDelay, InputMap inputMap) {
		super(turnMax, sleepDelay);
		this.inputMap = inputMap;
	}

	private void updateView() {
		final var isInvincible = false;
		final var isSick = false;
		final var featuresSnake = agents.stream()
			.map(a -> new FeaturesSnake(a.getPositions(), a.action(), a.color(), a.invincible(), a.sick()))
			.collect(Collectors.toCollection(ArrayList::new));

		final var featuresItem = items.stream()
			.map(i -> new FeaturesItem(i.position().x(), i.position().y(), i.type()))
			.collect(Collectors.toCollection(ArrayList::new));

		final var features = new Features(featuresSnake, featuresItem);
		pcs.firePropertyChange("features", null, features);
	}

	final protected void initializeGame() {
		agents = inputMap.getStart_snakes().stream()
			.map(fs -> AgentFabric.snake(
					new ArrayList<Position>(fs.getPositions()),
					fs.getLastAction(),
					fs.isInvincible(),
					fs.isSick(),
					fs.getColorSnake(),
					this)
			)
			.collect(Collectors.toCollection(ArrayList::new));

		items = inputMap.getStart_items().stream()
			.map(fi -> new Item(new Position(fi.getX(), fi.getY()), fi.getItemType()))
			.collect(Collectors.toCollection(ArrayList::new));

		updateView();
	}

	public boolean legalMove(Agent agent, AgentAction candidateAction) {
		final var pos = agent.nextPosition(candidateAction);
		final var walls = inputMap.get_walls();
		final boolean posIsNotAWall = !walls[pos.x()][pos.y()];
		return posIsNotAWall;
	}

	@Override
	final protected void takeTurn() {
		agents.forEach(a -> a.move());
		updateView();
	}

	@Override
	final protected void gameOver() {
		System.out.println("Game Over");
		return;
	}

	@Override
	final protected boolean gameContinue() {
		if (agents.size() == 0) {
			return false;
		}

		var agentsToRemove = new HashSet<Agent>();

		for (var a : agents) {
			var pos = a.getPositions();
			var head = pos.getFirst();

			// Check if an agent ran into himself
			var uniquePos = new HashSet(pos);
			boolean hasDuplicates = uniquePos.size() != pos.size();
			if (hasDuplicates) {
				agentsToRemove.add(a);
				continue;
			}

			// Check if an agent ran into a wall
			//
			// We only need to check the head because all the other body parts
			// follow the head, and therefore were previously a head, and were checked
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

// Contradiction dans les regles du jeux alors j'enleve ce cas qui pose probleme
/*
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


		return true;
	}

	public InputMap inputMap() {
		return inputMap;
	}
}
