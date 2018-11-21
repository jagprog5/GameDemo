package util;

import java.awt.Graphics2D;

/**
 * Simple object that stores position and speed as components.
 * Interfaces utilized for easy pass-by-reference.
 * @author John Giorshev
 */
public abstract class GraphicalObject {
	private FloatListener x;
	private FloatListener y;
	private FloatListener dx;
	private FloatListener dy;
	
	/**
	 * @deprecated If using this constructor, make sure to set properties manually!
	 */
	public GraphicalObject() {
		
	}
	
	public GraphicalObject(float x, float y, float dx, float dy) {
		this(new FloatListener() {
			@Override
			public float eval() {
				return x;
			}
		}, new FloatListener() {
			@Override
			public float eval() {
				return y;
			}
		}, new FloatListener() {
			@Override
			public float eval() {
				return dx;
			}
		}, new FloatListener() {
			@Override
			public float eval() {
				return dy;
			}
		});
	}

	public GraphicalObject(FloatListener x, FloatListener y, FloatListener dx, FloatListener dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}

	public abstract void updateMechanics();

	public abstract void draw(Graphics2D g2d);

	public float getX() {
		return x.eval();
	}

	public void setX(FloatListener x) {
		this.x = x;
	}
	
	public void setX(float x) {
		this.setX(new FloatListener() {
			@Override
			public float eval() {
				return x;
			}
		});
	}

	public float getY() {
		return y.eval();
	}

	public void setY(FloatListener y) {
		this.y = y;
	}
	
	public void setY(float y) {
		this.setY(new FloatListener() {
			@Override
			public float eval() {
				return y;
			}
		});
	}

	public float getDX() {
		return dx.eval();
	}

	public void setDX(FloatListener dx) {
		this.dx = dx;
	}
	
	public void setDX(float dx) {
		this.setDX(new FloatListener() {
			@Override
			public float eval() {
				return dx;
			}
		});
	}

	public float getDY() {
		return dy.eval();
	}

	public void setDY(FloatListener dy) {
		this.dy = dy;
	}
	
	public void setDY(float dy) {
		this.setDY(new FloatListener() {
			@Override
			public float eval() {
				return dy;
			}
		});
	}
	
	public void updatePosition() {
		float nextX = getX() + getDX();
		setX(new FloatListener() {
			@Override
			public float eval() {
				return nextX;
			}
		});
		float nextY = getY() + getDY();
		setY(new FloatListener() {
			@Override
			public float eval() {
				return nextY;
			}
		});
	}
}
