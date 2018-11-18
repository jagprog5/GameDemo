package util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * AnimationPanel acts as a constantly updating and drawing JPanel.
 * Acts as a small time step simulation.
 * Note: This does not include mouse of keyboard input.
 * @author John Giorshev
 */
public abstract class AnimationPanel extends JPanel implements ActionListener {
	private ArrayList<AffineTransform> transformationStack;
	private int frameCount;
	private int millisDelay = 16;
	private Timer timer;

	// More conventional game loops gave no noticeable differences.
	// http://www.java-gaming.org/index.php?topic=24220.0

	/**
	 * Class constructor. Remember to call start() to start begin the animation.
	 * <br>
	 * Automatically resizes to fit container after all pending AWT events have been
	 * processed.
	 * 
	 * @see #start()
	 */
	public AnimationPanel() {
		super();
		setDoubleBuffered(false);
		transformationStack = new ArrayList<AffineTransform>();
		timer = new Timer(millisDelay, this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		frameCount += 1;
		updateMechanics();
		standardRepaint();
	}

	/**
	 * Defaults to repainting the entire panel. 
	 * Override to change repaint area if desired.
	 */
	protected void standardRepaint() {
		repaint(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		draw(g2d);
	}

	/**
	 * Function called once every frame before draw(Graphics2D).
	 * 
	 * @see #draw(Graphics2D)
	 */
	public abstract void updateMechanics();

	/**
	 * Function called once every frame after each call of updateMechanics().
	 * 
	 * @see #updateMechanics()
	 */
	public abstract void draw(Graphics2D g2d);

	/**
	 * @return pauses the animation's timer.
	 * @see #start()
	 */
	public void pause() {
		timer.stop();
	}

	/**
	 * starts the animation's timer, which repeatedly calls
	 * updateMechanics() and draw(Graphics2D) respectively and repeatedly
	 *         with a delay specified by setTimerDelay(int).
	 * @see #pause()
	 * @see #updateMechanics()
	 * @see #draw(Graphics2D)
	 * @see #setTimerDelay(int)
	 */
	public AnimationPanel start() {
		timer.start();
		return this;
	}

	/**
	 * @return a boolean, false if the timer is running.
	 */
	public boolean isPaused() {
		return !timer.isRunning();
	}

	/**
	 * @return the timer delay between frames.
	 * @see #getTimerDelay()
	 */
	public int getTimerDelay() {
		return millisDelay;
	}

	/**
	 * Sets the delay between frames. Default is 15 which gives ~67 frames per
	 * second.
	 * 
	 * @param millis
	 *            sets the millisecond delay between frames.
	 * @see #getTimerDelay()
	 */
	public void setTimerDelay(int millis) {
		millisDelay = millis;
		timer.setDelay(millisDelay);
	}

	/**
	 * @deprecated
	 * @see #pause()
	 * @see #start()
	 * @see #isPaused()
	 * @see #getTimerDelay()
	 * @see #setTimerDelay(int)
	 */

	@Deprecated
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Gets the current frame count.
	 * Frames starts at frame #1.
	 * 
	 * @return the frame count.
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * Sets the current frame count.<br>
	 * Only use this function if you are certain it is necessary.
	 * 
	 * @param i
	 *            The current frame count will be set to this value.
	 * @return the frame count.
	 */
	public void setFrameCount(int i) {
		frameCount = i;
	}

	/**
	 * Stores the AffineTransform instance at the top of a stack.<br>
	 * To be recalled later by popMatrix().<br>
	 * A maximum of 128 AffineTransform objects can be stored.
	 * 
	 * @param at
	 *            the AffineTransform to store.
	 * @throws RuntimeException
	 *             if more than 128 AffineTransform objects are stored.
	 * @see #popMatrix()
	 * @see <a href="https://processing.org/">This functionality was inspired by Processing.</a>
	 */
	public void pushMatrix(AffineTransform at) {
		transformationStack.add(at);
		if (transformationStack.size() > 128) {
			// There is no way of recovering from this error.
			throw new RuntimeException("The transformation stack can only be pushed 64 times.");
		}
	}

	/**
	 * @return The AffineTransform at the top of the stack, which is then removed.
	 * @throws RuntimeException
	 *             if this method is called when the stack is empty.
	 * @see #pushMatrix(AffineTransform)
	 */
	public AffineTransform popMatrix() {
		if (transformationStack.size() < 1) {
			throw new RuntimeException("There is nothing to pop the matrix back to.");
		} else {
			AffineTransform g2d = transformationStack.get(transformationStack.size() - 1);
			transformationStack.remove(transformationStack.size() - 1);
			return g2d;
		}
	}

	/**
	 * @return the distance from the center of the panel to the corner of the panel.
	 */
	public float radialDist() {
		return GameMath.distance(0, 0, getWidth() / 2, getHeight() / 2);
	}

	/**
	 * @return A BufferedImage of the panel's contents. Returns a 1 x 1 white image
	 *         if the panel's width or height is smaller than 1.
	 */
	public BufferedImage getScreen() {
		Rectangle r = getBounds();
		BufferedImage bi = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_ARGB);
		paint(bi.getGraphics());
		return bi;
	}
}