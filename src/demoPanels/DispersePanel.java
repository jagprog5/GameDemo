package demoPanels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import util.GamePanel;
import util.GraphicalObject;

public class DispersePanel extends GamePanel {
	Integer startX;
	Integer startY;
	ArrayList<GraphicalObject> particles;

	public DispersePanel() {
		setBackground(Color.BLACK);
		particles = new ArrayList<GraphicalObject>();
		setTimerDelay(15);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Disperse.giveSize(getSize());
			}
		});
	}

	public boolean startPlotted() {
		return startX != null && startY != null;
	}

	@Override
	public void updateMechanics() {
		if (leftMouseJustPressed()) {
			startX = getMouseX();
			startY = getMouseY();
		}
		if (leftMouseReleased() && startPlotted()) {
			particles.add(
					new Disperse(startX, startY, (getMouseX() - startX) / 20, (getMouseY() - startY) / 20, particles));
			startX = null;
			startY = null;
		}
		for (int i = particles.size() - 1; i > -1; i--) {
			particles.get(i).updateMechanics();
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		if (startPlotted()) {
			g2d.setColor(Color.WHITE);
			g2d.drawLine(startX, startY, getMouseX(), getMouseY());
		}
		for (GraphicalObject go : particles) {
			go.draw(g2d);
		}
	}
}