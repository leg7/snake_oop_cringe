package Client.View;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Client.Controller.ControllerClient;
import Utils.AgentAction;
import Utils.Features;

public class ViewSnakeGame implements PropertyChangeListener, WindowStateListener {
	private JPanel panel;
	private JFrame frame;
	private PropertyChangeSupport pcs;

	public ViewSnakeGame(ControllerClient controller) {
		super();
		this.panel = new PanelSelection();
		this.pcs = new PropertyChangeSupport(this);
		this.frame = new JFrame("Snake game");

		controller.addPropertyChangeListener(this);
		panel.addPropertyChangeListener("snakeMapOptions", this);

		frame.add(panel);
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.addKeyListener(new MyKeyListener(this));

	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	public void propertyChange(PropertyChangeEvent e) {
		switch (e.getPropertyName()) {
			case "snakeMapOptions":
				pcs.firePropertyChange("mapSelected", null, e.getNewValue());
				break;
			case "features":
				var obj = e.getNewValue();
				if (obj instanceof

				Features(var fss, var fis) && panel instanceof PanelSnakeGame) {
					PanelSnakeGame panel = (PanelSnakeGame) this.panel;
					panel.updateInfoGame(fss, fis);
					panel.repaint();
				} else {
					System.exit(69);
				}
				break;

			case "turn":
				panel.repaint();
				break;

			default:
				System.exit(69);
		}

	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.frame.remove(this.panel);
		this.frame.add(panel);
		this.panel = panel;
		this.frame.setVisible(true);
	}

	public void setPanel(JPanel panel, Dimension d) {
		if (d != null)
			this.frame.setSize(d);
		setPanel(panel);
	}

	private class MyKeyListener implements KeyListener {
		ViewSnakeGame view;

		public MyKeyListener(ViewSnakeGame view) {
			this.view = view;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			final int keyCode = e.getKeyCode();

			System.out.println("Key pressed");
			AgentAction action;
			if (keyCode == KeyEvent.VK_LEFT) {
				action = AgentAction.MOVE_LEFT;
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				action = AgentAction.MOVE_RIGHT;
			} else if (keyCode == KeyEvent.VK_UP) {
				action = AgentAction.MOVE_UP;
			} else if (keyCode == KeyEvent.VK_DOWN) {
				action = AgentAction.MOVE_DOWN;
			} else
				return;

			view.pcs.firePropertyChange("action", null, action);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Handle key release events here
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// Handle key typed events here
		}
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSED) {
			pcs.firePropertyChange("running", true, false);
		}
	}

}
