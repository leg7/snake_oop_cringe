package Client.View;

import Client.Controller.ControllerClient;
import Client.View.PanelSnakeGame;
import Server.Model.Agent.AgentUserControlled;
import Utils.*;

import java.awt.Graphics;
import java.util.ArrayList;
import java.beans.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class ViewSnakeGame implements PropertyChangeListener, WindowStateListener {
	private PanelSnakeGame p;
	private JFrame frame;
	private boolean closed;
	private PropertyChangeSupport pcs;

	public boolean isClosed() {
		return closed;
	}

	public ViewSnakeGame(ControllerClient controller, PanelSnakeGame p) {
		super();
		this.p = p;
		pcs = new PropertyChangeSupport(this);
		controller.addPropertyChangeListener(this);
		frame = new JFrame("Snake game");
		frame.add(p);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			case "features":
				var obj = e.getNewValue();
				if (obj instanceof Features(var fss, var fis)) {
					p.updateInfoGame(fss, fis);
					p.repaint();
				} else {
					System.exit(69);
				}
				break;

			case "turn":
				p.repaint();
				break;

			default:
				System.exit(69);
		}
	}

	public void actionLeft() {
		pcs.firePropertyChange("action", null, "LEFT");
	}

	public void actionRight() {
		pcs.firePropertyChange("action", null, "RIGHT");
	}

	public void actionUp() {
		pcs.firePropertyChange("action", null, "UP");
	}

	public void actionDown() {
		pcs.firePropertyChange("action", null, "DOWN");
	}

	private class MyKeyListener implements KeyListener {
		private ViewSnakeGame view;

		public MyKeyListener(ViewSnakeGame view) {
			super();
			this.view = view;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			final int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_LEFT) {
				view.actionLeft();
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				view.actionRight();
			} else if (keyCode == KeyEvent.VK_UP) {
				view.actionUp();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				view.actionDown();
			}
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
