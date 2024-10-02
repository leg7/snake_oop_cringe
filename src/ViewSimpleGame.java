import javax.swing.*;
import java.awt.*;
import java.beans.*;

class ViewSimpleGame implements PropertyChangeListener {
	private JFrame jFrame;
	private JLabel jLabel;
	private Dimension windowSize;
	private GraphicsEnvironment ge;

	public ViewSimpleGame() {
		jFrame = new JFrame();
		jFrame.setTitle("Game");
		jFrame.setSize(new Dimension(700, 700));
		windowSize = jFrame.getSize();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 ;
		int dy = centerPoint.y - windowSize.height / 2 - 350;
		jFrame.setLocation(dx, dy);

		jLabel = new JLabel("Turn: ", JLabel.CENTER);
		jFrame.add(jLabel);

		jFrame.setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent e) {
		jLabel.setText("Turn :" + e.getNewValue());
	}
}
