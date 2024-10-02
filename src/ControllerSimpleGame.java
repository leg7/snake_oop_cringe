public class ControllerSimpleGame extends AbstractController {
	ViewSimpleGame viewSimpleGame;
	ViewCommand viewCommand;

	ControllerSimpleGame() {
		viewSimpleGame = new ViewSimpleGame();
		g = new SimpleGame(10, 500, viewSimpleGame);
		g.addPropertyChangeListener(viewSimpleGame);
		viewCommand = new ViewCommand(g, this);
	}
}
