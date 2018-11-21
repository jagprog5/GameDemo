package demoPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import util.GameMath;
import util.GraphicalObject;

class Disperse extends GraphicalObject {
	
	static void giveSize(Dimension d) {
		dim = d;
	}
	
	static Dimension dim;
	
	public static final float STARTSIZE = 50;
	public static final float DRIFT = 2;
	private ArrayList<GraphicalObject> arrayReference;
	private float size;

	/**
	 * If using this constructor, manually set properties.
	 */
	@SuppressWarnings("deprecation")
	public Disperse() {
		super();
	}
	
	public Disperse(float x, float y, float dx, float dy, ArrayList<GraphicalObject> ar) {
		// draw from center
		this(x -= STARTSIZE / 2, y -= STARTSIZE / 2, dx, dy, STARTSIZE, ar);
	}

	private Disperse(float x, float y, float dx, float dy, float size, ArrayList<GraphicalObject> ar) {
		super(x, y, dx, dy);
		this.size = size;
		arrayReference = ar;
	}
	
	public void breakUp() {
		//break into 1 or 2 pieces at a time.
		for (int i = (int)GameMath.nextFloat(1, 2); i > 0; i--) {
			Disperse d = new Disperse();
			genFromSelf(d);
			arrayReference.add(d);
		}
		genFromSelf(this); //Replacing all properties, but same object and reference.
	}

	@Override
	public void updateMechanics() {
		size -= 0.25f;
		if (size < 5) {
			arrayReference.remove(this);
			//less and less likely to break apart if small
		} else if ((int)GameMath.nextFloat(0, 3 + (1-size/STARTSIZE) * 100) == 0) {
			breakUp();
		}
		if (getX() > dim.getWidth() - size) {
			setX((float)dim.getWidth() - size);
			setDX(-Math.abs(getDX()));
		}
		if (getX() < 0) {
			setX(0);
			setDX(Math.abs(getDX()));
		}
		if (getY() > dim.getHeight() - size) {
			setY((float)dim.getHeight() - size);
			setDY(-Math.abs(getDY()));
		}
		if (getY() < 0) {
			setY(0);
			setDY(Math.abs(getDY()));
		}
		updatePosition();
	}
	
	public void genFromSelf(Disperse d) {
		d.size = size - 1;
		d.setDX(getDX() + GameMath.nextFloat(-DRIFT, DRIFT));
		d.setDY(getDY() + GameMath.nextFloat(-DRIFT, DRIFT));
		d.setX(getX());
		d.setY(getY());
		d.arrayReference = arrayReference;
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(1, 1, 1, size / STARTSIZE));
		g2d.fill(new Ellipse2D.Float(getX(), getY(), size, size));
	}
}