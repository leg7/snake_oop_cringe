package Client.View;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PanelSelection extends JPanel {

	public static final long serialVersionUID = 1L;

	private ButtonGroup selectMode;
	private ButtonGroup selectSize;
	private ButtonGroup selectWall;
	private JButton confirm;

	public PanelSelection() {
		super();

		JLabel titleLabel = new JLabel("<html><h1>Select map options</h1></html>", JLabel.CENTER);

		JLabel modeLabel = new JLabel("Mode:");
		JRadioButton modeSolo = new JRadioButton("Solo");
		JRadioButton modePvP = new JRadioButton("PvP");
		JRadioButton modePvE = new JRadioButton("PvE");
		selectMode = new ButtonGroup();
		selectMode.add(modeSolo);
		selectMode.add(modePvP);
		selectMode.add(modePvE);
		selectMode.setSelected(modeSolo.getModel(), true);

		JLabel sizeLabel = new JLabel("Size:");
		JRadioButton sizeNormal = new JRadioButton("Normal");
		JRadioButton sizeSmall = new JRadioButton("Small");
		selectSize = new ButtonGroup();
		selectSize.add(sizeNormal);
		selectSize.add(sizeSmall);
		selectSize.setSelected(sizeNormal.getModel(), true);

		JLabel wallLabel = new JLabel("Walls:");
		JRadioButton wallYes = new JRadioButton("Yes");
		JRadioButton wallNo = new JRadioButton("No");
		selectWall = new ButtonGroup();
		selectWall.add(wallYes);
		selectWall.add(wallNo);
		selectWall.setSelected(wallNo.getModel(), true);

		confirm = new JButton("Confirm");
		confirm.addActionListener(new ConfirmActionListener());

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel optionsPanel = new JPanel();
		GroupLayout layout = new GroupLayout(optionsPanel);
		optionsPanel.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		Group hgroup = layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(modeLabel)
						.addComponent(sizeLabel)
						.addComponent(wallLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(modeSolo)
						.addComponent(sizeNormal)
						.addComponent(wallYes))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(modePvP)
						.addComponent(sizeSmall)
						.addComponent(wallNo))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(modePvE));
		Group vgroup = layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(modeLabel)
						.addComponent(modeSolo)
						.addComponent(modePvP)
						.addComponent(modePvE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(sizeLabel)
						.addComponent(sizeNormal)
						.addComponent(sizeSmall))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(wallLabel)
						.addComponent(wallYes)
						.addComponent(wallNo));
		layout.setHorizontalGroup(hgroup);
		layout.setVerticalGroup(vgroup);

		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		confirm.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(titleLabel);
		add(optionsPanel);
		add(confirm);
	}

	public enum Mode {
		Solo, PvP, PvE
	}

	public enum Size {
		Normal, Small
	}

	public record MapOptions(Mode mode, Size size, boolean wall) {
	}

	private class ConfirmActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			Mode mode = Mode.Solo;
			Size size = Size.Normal;
			boolean wall = false;

			for (var e = selectMode.getElements(); e.hasMoreElements();) {
				AbstractButton b = e.nextElement();
				if (b.isSelected())
					switch (b.getText()) {
						case "PvP":
							mode = Mode.PvP;
							break;
						case "PvE":
							mode = Mode.PvE;
							break;
						default:
							mode = Mode.Solo;
					}
			}

			for (var e = selectSize.getElements(); e.hasMoreElements();) {
				AbstractButton b = e.nextElement();
				if (b.isSelected())
					switch (b.getText()) {
						case "Small":
							size = Size.Small;
							break;
						default:
							size = Size.Normal;
					}
			}

			for (var e = selectWall.getElements(); e.hasMoreElements();) {
				AbstractButton b = e.nextElement();
				if (b.isSelected())
					wall = "Yes".equals(b.getText());
			}

			firePropertyChange("snakeMapOptions", null, new MapOptions(mode, size, wall));
		}
	}
}
