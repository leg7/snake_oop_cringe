package leonardgomez.Controller;

import leonardgomez.Controller.AbstractController;
import leonardgomez.Model.InputMap;
import leonardgomez.Model.SnakeGame;
import leonardgomez.View.*;
import java.util.ArrayList;
import utils.*;

public class ControllerSnakeGame extends AbstractController {
	ViewCommand viewCommand;
	ViewSnakeGame viewSnakeGame;

	public ControllerSnakeGame() {
		InputMap im = null;
		try {
			im = new InputMap("/persistent/home/user/documents/education/universite_angers/mi/m1/blocs/design-patterns/projet/bin/layouts/aloneNoWall.lay");
		} catch (Exception e) {}

		// View
		var p = new PanelSnakeGame(im.getSizeX(), im.getSizeY(), im.get_walls(), im.getStart_snakes(), im.getStart_items());
		viewSnakeGame = new ViewSnakeGame(p);

		// Model

		this.g = new SnakeGame(10, 100, im);
		this.g.addPropertyChangeListener(viewSnakeGame);

		// Command
		viewCommand = new ViewCommand(g, this);
	}
}
