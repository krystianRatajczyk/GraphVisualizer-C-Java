package view;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
	private boolean showIds = false;
	private boolean showEdgeThickness = false;

	public Canvas() {
		setBackground(Color.WHITE);
	}

	public void setShowIds(boolean show) {
		this.showIds = show;
		repaint();
	}

	public void setShowEdgeThickness(boolean show) {
		this.showEdgeThickness = show;
		repaint();
	}

	public void clear() {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (showIds) {
			g.setColor(Color.GRAY);
			g.drawString("IDs visible", 10, 20);
		}
		if (showEdgeThickness) {
			g.setColor(Color.GRAY);
			g.drawString("Edge thickness shown", 10, 40);
		}
	}
}
