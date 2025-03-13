package Client.View;

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

public class ViewSnakeGame implements PropertyChangeListener {
	private PanelSnakeGame p;
	private JFrame frame;

	public ViewSnakeGame(PanelSnakeGame p) {
		super();
		this.p = p;
		frame = new JFrame("Snake game");
		frame.add(p);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent e) {
		switch (e.getPropertyName()) {
			case "features":
				var obj = e.getNewValue();
				if (obj instanceof Features(var fss, var fis)) {
					p.updateInfoGame(fss, fis);
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

	public void addAgentUserControlled(AgentUserControlled a) {
		frame.addKeyListener(new MyKeyListener(a));
	}

	private class MyKeyListener implements KeyListener {
		private AgentUserControlled a;

		public MyKeyListener(AgentUserControlled a) {
			super();
			this.a = a;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			final int keyCode = e.getKeyCode();
			final int kl = a.keybindings().key_left();
			final int kr = a.keybindings().key_right();
			final int ku = a.keybindings().key_up();
			final int kd = a.keybindings().key_down();

			if (keyCode == kl) {
				a.setAction(AgentAction.MOVE_LEFT);
			} else if (keyCode == kr) {
				a.setAction(AgentAction.MOVE_RIGHT);
			} else if (keyCode == ku) {
				a.setAction(AgentAction.MOVE_UP);
			} else if (keyCode == kd) {
				a.setAction(AgentAction.MOVE_DOWN);
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
}
