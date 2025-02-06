package leonardgomez.Controller;

import leonardgomez.Controller.AbstractController;
import leonardgomez.Model.*;
import leonardgomez.Model.Agent.*;
import leonardgomez.View.*;
import java.util.ArrayList;
import utils.*;

public class ControllerSnakeGame extends AbstractController {
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
		//var sg = new SnakeGameAI(turns, turnTime, im);
		 var sg = new SnakeGamePVP(turns, turnTime, im, viewSnakeGame);

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
}
