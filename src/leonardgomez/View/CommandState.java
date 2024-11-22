package leonardgomez.View;

public abstract class CommandState {
	protected ViewCommand view;

	CommandState(ViewCommand view) {
		this.view = view;
		// System.out.println(this.getClass().getSimpleName());
	}

	public abstract void play();
	public abstract void pause();
	public abstract void restart();
	public abstract void step();
}
