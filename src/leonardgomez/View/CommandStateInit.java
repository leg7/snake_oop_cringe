package leonardgomez.View;

public class CommandStateInit extends CommandState {
	CommandStateInit(ViewCommand view) {
		super(view);
		view.buttonRestart.setEnabled(false);
		view.buttonPause.setEnabled(false);
		view.buttonStep.setEnabled(true);
		view.buttonPlay.setEnabled(true);
	}

	@Override
	public void play() {
		view.setCommandState(new CommandStatePlay(view));
	}

	@Override
	public void pause() {
		return;
	}

	@Override
	public void restart() {
		return;
	}

	@Override
	public void step() {
		view.setCommandState(new CommandStatePause(view));
	}
}
