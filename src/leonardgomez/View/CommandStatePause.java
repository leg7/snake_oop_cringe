package leonardgomez.View;

public class CommandStatePause extends CommandState {
	CommandStatePause(ViewCommand v) {
		super(v);
		v.buttonPause.setEnabled(false);
		v.buttonPlay.setEnabled(true);
		v.buttonRestart.setEnabled(true);
		v.buttonStep.setEnabled(true);
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
		view.setCommandState(new CommandStateInit(view));
	}

	@Override
	public void step() {
		return;
	}
}
