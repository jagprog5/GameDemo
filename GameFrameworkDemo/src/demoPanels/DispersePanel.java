package demoPanels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

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
					new Disperse(startX, startY, (getMouseX() - startX) / 10, (getMouseY() - startY) / 10, particles));
			startX = null;
			startY = null;
		}
		for (int i = 0; i < particles.size(); i++) {
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