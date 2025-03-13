package Client.Controller;

import Client.View.ViewSnakeGame;
import Client.View.PanelSnakeGame;
import Server.Model.InputMap;

class ControllerClient {
	ViewSnakeGame viewSnakeGame;

	public ControllerClient() {
		var im = inputMap("/layouts/arenaNoWall.lay");

		var p = new PanelSnakeGame(
			im.getSizeX(), im.getSizeY(),
			im.get_walls(),
			im.getStart_snakes(), im.getStart_items()
		);
		viewSnakeGame = new ViewSnakeGame(p);
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
