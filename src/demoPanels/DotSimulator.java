package demoPanels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import util.AnimationPanel;
import util.GameMath;
import util.GraphicalObject;

public class DotSimulator extends AnimationPanel {
	ArrayList<Dot> dots;

	public DotSimulator() {
		setBackground(new Color(0f, 0f, 0f, 0.05f));
		dots = new ArrayList<Dot>();
		int size = (int) GameMath.nextFloat(10, 15);//5, 10);

		// size only obtained after panel is added.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setBackground(Color.black);
				for (int i = 0; i < size; i++) {
					dots.add(new Dot(GameMath.nextFloat(0, getWidth()), GameMath.nextFloat(0, getHeight()),
							GameMath.nextFloat(-40, 40), GameMath.nextFloat(-40, 40)));
				}
			}

		});
	}

	@Override
	public void updateMechanics() {
		for (int i = dots.size() - 1; i > -1; i--) {
			dots.get(i).updateMechanics();
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		for (Dot d : dots) {
			d.draw(g2d);
		}
	}

	class Dot extends GraphicalObject {
		public static final int SIZEMIN = 3;
		public static final int SIZEMAX = 40;
		float size = 0;

		public Dot(float x, float y) {
			this(x, y, 0, 0);
		}

		public Dot(float x, float y, float dx, float dy) {
			super(x, y, dx, dy);
			size = GameMath.nextFloat(SIZEMIN, SIZEMAX);
		}

		@Override
		public void updateMechanics() {
			for (Dot d : dots) {
				// attract to everything except yourself.
				if (!d.equals(this)) {
					// increase with distance
					float[] attrac = GameMath.projectileSpeed(getX(), getY(), d.getX(), d.getY(),
							(GameMath.distance(getX(), getY(), d.getX(), d.getY()) / 100000) * d.size);
					setDX(getDX() + attrac[0]);
					setDY(getDY() + attrac[1]);
				}
			}

			//slight pull towards center
			float[] deaden = GameMath.projectileSpeed(getX(), getY(), getWidth() / 2f, getHeight() / 2f, 0.1f);
			setDX(getDX() + deaden[0]);
			setDY(getDY() + deaden[1]);

			// Deaden velocity when bounding off wall.
			//It was actually quite hard to find a way of making the
			//simulator never fall into any equilibrium. This works,
			//and keeps it deterministic after start conditions set.
			updatePosition();
			if (getX() > getWidth()) {
				setX(getWidth());
				setDX(-Math.abs(getDX()*0.9f));
				setDY(getDY() * 0.9f);
			}
			if (getX() < 0) {
				setX(0);
				setDX(Math.abs(getDX() * 0.9f));
				setDY(getDY() * 0.9f);
			}
			if (getY() > getHeight()) {
				setY(getHeight());
				setDY(-Math.abs(getDY() * 0.9f));
				setDX(getDX() * 0.9f);
			}
			if (getY() < 0) {
				setY(0);
				setDY(Math.abs(getDY() * 0.9f));
				setDX(getDX() * 0.9f);
			}
		}

		@Override
		public void draw(Graphics2D g2d) {
			float gradient = (size - SIZEMIN) / (SIZEMAX - SIZEMIN);
			g2d.setColor(new Color(gradient, 1 - gradient, 0f));
			g2d.fill(new Ellipse2D.Float(getX() - size / 2, getY() - size / 2, size, size));
		}
	}
}
