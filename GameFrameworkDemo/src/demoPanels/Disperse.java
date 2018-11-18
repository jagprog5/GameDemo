package demoPanels;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import util.GraphicalObject;

class Disperse extends GraphicalObject {
	public static final float STARTSIZE = 20;
	public static final float DRIFT = 5;
	public static final Random r = new Random();
	private ArrayList<GraphicalObject> arrayReference;
	private float size;

	public Disperse(float x, float y, float dx, float dy, ArrayList<GraphicalObject> ar) {
		//draw from center
		this(x -= STARTSIZE / 2, y -= STARTSIZE / 2, dx, dy, STARTSIZE, ar);
	}

	private Disperse(float x, float y, float dx, float dy, float size, ArrayList<GraphicalObject> ar) {
		super(x, y, dx, dy);
		this.size = size;
		arrayReference = ar;
	}

	@Override
	public void updateMechanics() {
		size -= r.nextFloat() / 10;
		if (size < 5) {
			arrayReference.remove(this);
		} else if (r.nextInt(5) == 0) {
			arrayReference.remove(this);
			setDX(getDX() + r.nextFloat() * DRIFT - DRIFT / 2);
			setDY(getDY() + r.nextFloat() * DRIFT - DRIFT / 2);
			for (int i = r.nextInt(2) + 1; i > 0; i--) {
				arrayReference.add(new Disperse(getX(), getY(), getDX(), getDY(), size - 1, arrayReference));
			}
		}
		updatePosition();
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(1, 1, 1, size / STARTSIZE));
		g2d.fill(new Ellipse2D.Float(getX(), getY(), size, size));
	}
}