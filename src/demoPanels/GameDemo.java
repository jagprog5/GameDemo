package demoPanels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import Assets.Images;
import util.GameMath;
import util.GamePanel;
import util.GraphicalObject;

public class GameDemo extends GamePanel {
	ArrayList<GraphicalObject> particles;
	ArrayList<BossPart> boss;
	boolean gameStarted = false;
	boolean won = false;
	float sizeSum; // calculate when boss created. Used for weighted average.
	float bossCenterX;
	float bossCenterY;
	float bossHealth;
	boolean charging;
	int prevPlayerX;
	int prevPlayerY;
	int playerX;
	int playerY;
	boolean didJump = false;
	float shotDelay = 0;

	public GameDemo() {
		super();
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "blank cursor"));
		particles = new ArrayList<GraphicalObject>();
		boss = new ArrayList<BossPart>();
		setBackground(new Color(0f, 0f, 0f, 0.1f));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}

	@Override
	public void updateMechanics() {
		// restrict movement to prevent jumping
		didJump = false;
		if (gameStarted) {
			if (shotDelay > 0)
				shotDelay--;
			if (GameMath.distance(getMouseX(), getMouseY(), prevPlayerX, prevPlayerY) > 20) {
				float[] restricted = GameMath.projectileSpeed(prevPlayerX, prevPlayerY, getMouseX(), getMouseY(), 20);
				playerX = prevPlayerX + (int) restricted[0];
				playerY = prevPlayerY + (int) restricted[1];
				didJump = true;
			} else {
				playerX = getMouseX();
				playerY = getMouseY();
			}
		} else {
			playerX = getMouseX();
			playerY = getMouseY();
		}
		charging = (getFrameCount() + 1300) / 500 % 3 == 0;
		if (gameStarted) {
			float sumX = 0;
			float sumY = 0;
			for (BossPart b : boss) {
				b.updateMechanics();
				sumX += b.getX() * b.size;
				sumY += b.getY() * b.size;
			}
			for (int i = particles.size() - 1; i > -1; i--) {
				particles.get(i).updateMechanics();
			}
			bossCenterX = sumX / sizeSum;
			bossCenterY = sumY / sizeSum;
			if (!charging && getFrameCount() > 200
					&& getFrameCount() % (getFrameCount() / 100 % 3 == 0 ? 5 : 20) == 0) {
				float[] out = GameMath.projectileSpeed(bossCenterX, bossCenterY, playerX, playerY, bossHealth < 25 ? 7 : 5);
				particles.add(new Laser(bossCenterX, bossCenterY, out[0], out[1]));
			}
		} else {
			if (leftMouseReleased() && playerX > getWidth() / 2 - 50 && playerX < getWidth() / 2 + 50
					&& playerY > 3 * getHeight() / 4 - 30 && playerY < 3 * getHeight() / 4 + 30) {
				setFrameCount(0); // boss phases relies on frame count
				gameStarted = true;
				bossHealth = 100;
				won = false;
				boss.clear();
				particles.clear();
				bossCenterX = -40;
				bossCenterY = -40;
				sizeSum = 0;
				for (int i = 0; i < 15; i++) {
					boss.add(new BossPart(GameMath.nextFloat(getWidth() / 3, 2 * getWidth() / 3),
							GameMath.nextFloat(getHeight() / 5, getHeight() / 2), 0, 0));
					sizeSum += boss.get(i).size;
				}
			}
		}
		if (shotDelay == 0) {
			shotDelay = 20;
			if (charsDown().contains('w') || charsDown().contains('W') || keyCodesDown().contains(KeyEvent.VK_UP)) {
				particles.add(new PlayerLaser(playerX, playerY, 0, -10));
			} else if (charsDown().contains('s') || charsDown().contains('S')
					|| keyCodesDown().contains(KeyEvent.VK_DOWN)) {
				particles.add(new PlayerLaser(playerX, playerY, 0, 10));
			} else if (charsDown().contains('a') || charsDown().contains('A')
					|| keyCodesDown().contains(KeyEvent.VK_LEFT)) {
				particles.add(new PlayerLaser(playerX, playerY, -10, 0));
			} else if (charsDown().contains('d') || charsDown().contains('D')
					|| keyCodesDown().contains(KeyEvent.VK_RIGHT)) {
				particles.add(new PlayerLaser(playerX, playerY, 10, 0));
			}
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		if (gameStarted) {
			int i = 0;
			for (BossPart b : boss) {
				i++;
				if (i == 8) {
					g2d.drawImage(charging ? FACEHURT : FACE, (int) bossCenterX - 25, (int) bossCenterY - 25, 50, 50,
							null);
					for (GraphicalObject c : particles) {
						c.draw(g2d);
					}
				}
				b.draw(g2d);
			}

			g2d.setColor(new Color(bossHealth < 25 ? (bossHealth < 15 ? 0.9f : 0.7f) : 0.5f, 0.1f, 0.1f, 0.3f));
			g2d.fill(new Rectangle2D.Float(10, 10, (getWidth() - 20) * bossHealth / 100, 40));
		} else {
			Stroke s = g2d.getStroke();
			g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.CAP_BUTT));
			g2d.setColor(new Color(0.9f, 0.3f, 0.3f));
			g2d.setColor(leftMousePressed() && playerX > getWidth() / 2 - 50 && playerX < getWidth() / 2 + 50
					&& playerY > 3 * getHeight() / 4 - 30 && playerY < 3 * getHeight() / 4 + 30 ? Color.RED
							: Color.GREEN);
			g2d.draw((new Rectangle2D.Float(getWidth() / 2 - 50, (3 * getHeight()) / 4 - 30, 100, 60)));
			g2d.setStroke(s);
			g2d.setFont(new Font("sansserif", Font.PLAIN, 15));
			g2d.drawString("Click Here", getWidth() / 2 - 35, (3 * getHeight()) / 4 + 5);
			
			if (won) {
				g2d.setFont(new Font("sansserif", Font.PLAIN, 40));
				g2d.drawString("YOU WON!", getWidth() / 2 - g2d.getFontMetrics().stringWidth("YOU WON!") / 2,
						getHeight() / 4);
			}
		}
		Stroke s = g2d.getStroke();
		g2d.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
		g2d.setColor(new Color(0.3f, 0.7f, 0.3f));
		float jitter = 10;
		g2d.drawLine((int) (playerX + GameMath.nextFloat(-jitter, jitter)),
				(int) (playerY + GameMath.nextFloat(-jitter, jitter)),
				(int) (prevPlayerX + GameMath.nextFloat(-jitter, jitter)),
				(int) (prevPlayerY + GameMath.nextFloat(-jitter, jitter)));
		if (didJump) {
			g2d.setColor(new Color(0.7f, 0.7f, 0.3f, 0.5f));
			g2d.fill(new Ellipse2D.Float(getMouseX() - 10, getMouseY() - 10, 20, 20));
		}
		g2d.setStroke(s);
		prevPlayerX = playerX;
		prevPlayerY = playerY;
	}

	public static final Image FIREBALL = Images.getImage(Images.FIREBALL);
	public static final Image FACE = Images.getImage(Images.FACE);
	public static final Image HOTFIREBALL = Images.getImage(Images.HOTFIREBALL);
	public static final Image FACEHURT = Images.getImage(Images.FACEHURT);

	class BossPart extends GraphicalObject {
		public static final int SIZEMIN = 5;
		public static final int SIZEMAX = 40;
		float rotDelta;
		float size;
		private float rot = 0;

		public BossPart(float x, float y, float dx, float dy) {
			super(x, y, dx, dy);
			rotDelta = GameMath.nextFloat(-0.1f, 0.1f);
			size = GameMath.nextFloat(SIZEMIN, SIZEMAX);
		}

		@Override
		public void updateMechanics() {
			rot += rotDelta; // keep spinning
			for (BossPart d : boss) {
				// attract to all other boss parts.
				if (!d.equals(this)) {
					// increase with distance
					float[] attrac = GameMath.projectileSpeed(getX(), getY(), d.getX(), d.getY(),
							(GameMath.distance(getX(), getY(), d.getX(), d.getY()) / 100000) * d.size);
					setDX(getDX() + attrac[0]);
					setDY(getDY() + attrac[1]);
				}
			}

			if (charging) {
				// charging towards player
				float[] track = GameMath.projectileSpeed(getX(), getY(), playerX, playerY, bossHealth < 15 ? 0.2f : 0.1f);
				setDX(getDX() + track[0]);
				setDY(getDY() + track[1]);
			} else {
				// drag force when not charging
				float[] drag = GameMath.projectileSpeed(getDX(), getDY(), 0, 0,
						GameMath.distance(getDX(), getDY(), 0, 0) / 800f);
				setDX(getDX() + drag[0]);
				setDY(getDY() + drag[1]);
			}

			// keep random jitter
			if (GameMath.random.nextInt(70) == 0) {
				float[] hit = GameMath.spawnPoint(1f, 2f);
				setDX(getDX() + hit[0]);
				setDY(getDY() + hit[1]);
			}

			// Deaden velocity when bounding off wall. Reduce speed more when not charging
			float thud = charging ? 0.9f : 0.4f;
			updatePosition();
			if (getX() > getWidth() - 10) {
				setX(getWidth() - 10);
				setDX(-Math.abs(getDX() * thud));
				setDY(getDY() * thud);
			}
			if (getX() < 10) {
				setX(10);
				setDX(Math.abs(getDX() * thud));
				setDY(getDY() * thud);
			}
			if (getY() > getHeight() - 10) {
				setY(getHeight() - 10);
				setDY(-Math.abs(getDY() * thud));
				setDX(getDX() * thud);
			}
			if (getY() < 10) {
				setY(10);
				setDY(Math.abs(getDY() * thud));
				setDX(getDX() * thud);
			}

			if (GameMath.distance(getX(), getY(), playerX, playerY) < (size < 10 ? 10 : size)) {
				gameStarted = false;
			}
		}

		@Override
		public void draw(Graphics2D g2d) {
			pushMatrix(g2d.getTransform());
			g2d.translate(getX(), getY());
			g2d.rotate(rot);
			g2d.drawImage(charging ? HOTFIREBALL : FIREBALL, (int) (-size / 2), (int) (-size / 2), (int) size,
					(int) size, null);
			g2d.setTransform(popMatrix());
		}
	}

	class Laser extends GraphicalObject {
		public static final int SIZE = 20;

		public Laser(float x, float y, float dx, float dy) {
			super(x, y, dx, dy);
		}

		@Override
		public void updateMechanics() {
			updatePosition();
			if (getX() < -40) {
				particles.remove(this);
			} else if (getX() > getWidth() + 40) {
				particles.remove(this);
			}
			if (getY() < -40) {
				particles.remove(this);
			} else if (getY() > getHeight() + 40) {
				particles.remove(this);
			}
			if (GameMath.distance(playerX, playerY, getX(), getY()) < SIZE / 2 + 10) {
				gameStarted = false;
			}
		}

		@Override
		public void draw(Graphics2D g2d) {
			Stroke s = g2d.getStroke();
			g2d.setStroke(new BasicStroke(SIZE, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
			g2d.setColor(new Color(0.5f, 0, 0.9f));
			float jitter = 10;
			g2d.drawLine((int) (getX() + GameMath.nextFloat(-jitter, jitter)),
					(int) (getY() + GameMath.nextFloat(-jitter, jitter)),
					(int) (getX() + getDX() + GameMath.nextFloat(-jitter, jitter)),
					(int) (getY() + getDY() + GameMath.nextFloat(-jitter, jitter)));
			g2d.setStroke(s);
		}
	}

	class PlayerLaser extends GraphicalObject {
		public static final int SIZE = 10;

		public PlayerLaser(float x, float y, float dx, float dy) {
			super(x, y, dx, dy);
		}

		@Override
		public void updateMechanics() {
			updatePosition();
			if (getX() < -40) {
				particles.remove(this);
			} else if (getX() > getWidth() + 40) {
				particles.remove(this);
			}
			if (getY() < -40) {
				particles.remove(this);
			} else if (getY() > getHeight() + 40) {
				particles.remove(this);
			}
			if (GameMath.distance(bossCenterX, bossCenterY, getX(), getY()) < 30) {
				particles.remove(this);
				bossHealth -= 2.5f;
				if (bossHealth <= 0) {
					won = true;
					gameStarted = false;
				}
			}
		}

		@Override
		public void draw(Graphics2D g2d) {
			Stroke s = g2d.getStroke();
			g2d.setStroke(new BasicStroke(SIZE, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
			g2d.setColor(new Color(0.7f, 0.9f, 0.9f));
			float jitter = 10;
			g2d.drawLine((int) (getX() + GameMath.nextFloat(-jitter, jitter)),
					(int) (getY() + GameMath.nextFloat(-jitter, jitter)),
					(int) (getX() + getDX() + GameMath.nextFloat(-jitter, jitter)),
					(int) (getY() + getDY() + GameMath.nextFloat(-jitter, jitter)));
			g2d.setStroke(s);
		}
	}
}
