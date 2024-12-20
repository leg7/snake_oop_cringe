package leonardgomez.Controller;

import leonardgomez.View.*;
import leonardgomez.Model.*;

public class ControllerSimpleGame extends AbstractController {
	ViewSimpleGame viewSimpleGame;
	ViewCommand viewCommand;

	public ControllerSimpleGame() {
		viewSimpleGame = new ViewSimpleGame();

		g = new SimpleGame(10, 500);
		g.addPropertyChangeListener(viewSimpleGame);
		g.init();

		viewCommand = new ViewCommand(g, this);
	}
}
