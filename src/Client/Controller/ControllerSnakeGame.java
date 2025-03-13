package Client.Controller;

import Client.View.*;
import java.util.ArrayList;
import Utils.*;

public class ControllerSnakeGame {
	/*
	ViewCommand viewCommand;
	ViewSnakeGame viewSnakeGame;

	public ControllerSnakeGame() {
		var im = inputMap("/layouts/arenaNoWall.lay");

		var p = new PanelSnakeGame(
			im.getSizeX(), im.getSizeY(),
			im.get_walls(),
			im.getStart_snakes(), im.getStart_items()
		);
		viewSnakeGame = new ViewSnakeGame(p);

		var turns = 1000;
		var turnTime = 50;
		var sg = new SnakeGameAI(turns, turnTime, im);
		// var sg = new SnakeGamePVP(turns, turnTime, im, viewSnakeGame);

		this.g = sg;
		g.init();
		this.g.addPropertyChangeListener(viewSnakeGame);

		viewCommand = new ViewCommand(g, this);
	}

	public InputMap inputMap(String filename) {
		try {
			return new InputMap(filename);
		} catch (Exception e) {
			System.out.println("Couldn't import layout file");
			System.exit(69);
		}
		return null;
	}
	*/
}
