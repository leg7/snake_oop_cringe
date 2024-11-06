public class CommandStatePlay extends CommandState {
	CommandStatePlay(ViewCommand v) {
		super(v);
		v.buttonPlay.setEnabled(false);
		v.buttonStep.setEnabled(false);
		v.buttonRestart.setEnabled(true);
		v.buttonPause.setEnabled(true);
	}
	@Override
	public void play() {
		return;
	}

	@Override
	public void pause() {
		view.setCommandState(new CommandStatePause(view));
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
