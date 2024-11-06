import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.*;

class ViewCommand implements PropertyChangeListener {
	private JFrame jFrame;
	private Dimension windowSize;
	private GraphicsEnvironment ge;
	private JLabel turnLabel;

	private AbstractController controller;
	public JButton buttonRestart;
	public JButton buttonPause;
	public JButton buttonPlay;
	public JButton buttonStep;

	private CommandState commandState;

	public ViewCommand(Game game, AbstractController controller) {
		game.addPropertyChangeListener(this);

		jFrame = new JFrame();
		jFrame.setTitle("Commands");
		jFrame.setSize(new Dimension(700, 700));
		windowSize = jFrame.getSize();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 ;
		int dy = centerPoint.y - windowSize.height / 2 - 350;
		jFrame.setLocation(dx, dy);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 1));
		jFrame.add(mainPanel);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 4));
		buttonRestart = new JButton(new ImageIcon("../icons/restart.png"));
		buttonPause   = new JButton(new ImageIcon("../icons/pause.png"));
		buttonPlay    = new JButton(new ImageIcon("../icons/play.png"));
		buttonStep    = new JButton(new ImageIcon("../icons/step.png"));
		buttonsPanel.add(buttonRestart);
		buttonsPanel.add(buttonPause);
		buttonsPanel.add(buttonPlay);
		buttonsPanel.add(buttonStep);
		mainPanel.add(buttonsPanel);

		this.controller = controller;
		commandState = new CommandStateInit(this);
		buttonRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				controller.restart();
				commandState.restart();
			}
		});
		buttonPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				controller.pause();
				commandState.pause();
			}
		});
		buttonPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				controller.play();
				commandState.play();
			}
		});
		buttonStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				controller.step();
				commandState.step();
			}
		});

		JPanel sliderAndInfoPanel = new JPanel();
		sliderAndInfoPanel.setLayout(new GridLayout(1, 2));
		JSlider slider = new JSlider(1, 10, 1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		sliderAndInfoPanel.add(slider);
		mainPanel.add(sliderAndInfoPanel);
		turnLabel = new JLabel("Turn: 0", JLabel.CENTER);
		sliderAndInfoPanel.add(turnLabel);

		jFrame.setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent e) {
		turnLabel.setText("Turn: " + e.getNewValue());
	}

	public void setCommandState(CommandState n) {
		commandState = n;
	}
}
