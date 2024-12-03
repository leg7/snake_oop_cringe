package leonardgomez.View;

import leonardgomez.View.PanelSnakeGame;
import java.awt.Graphics;
import java.util.ArrayList;
import java.beans.*;
import java.awt.*;
import javax.swing.*;

public class ViewSnakeGame implements PropertyChangeListener {
	private PanelSnakeGame p;
	private JFrame frame;

	public ViewSnakeGame(PanelSnakeGame p) {
		this.p = p;
		frame = new JFrame("Snake game");
		frame.add(p);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent e) {
		 p.repaint();
	}
}
