package demoPanels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import util.GamePanel;

public class MouseKeyInputDisplay extends GamePanel {
	int scroll = 45;

	public MouseKeyInputDisplay() {
		setBackground(Color.BLACK);
		setTimerDelay(15);
	}

	@Override
	public void updateMechanics() {
		scroll += getMouseWheelChange();
		if (scroll < 5)
			scroll = 5;
		if (scroll > 85)
			scroll = 85;
	}

	@Override
	public void draw(Graphics2D g2d) {
		// I don't care about window scaling, so I'm just using values on screen for
		// object positioning.

		// Mouse stuff
		g2d.setFont(new Font("sansserif", Font.PLAIN, 20));
		g2d.setColor(leftMousePressed() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(5, 5, 40, 40));

		g2d.setColor(rightMousePressed() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(50, 5, 40, 40));

		g2d.setColor(leftMouseJustPressed() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(5, 50, 40, 40));

		g2d.setColor(rightMouseJustPressed() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(50, 50, 40, 40));

		g2d.setColor(leftMouseReleased() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(5, 95, 40, 40));

		g2d.setColor(rightMouseReleased() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(50, 95, 40, 40));

		// scroll wheel display
		g2d.setColor(Color.DARK_GRAY);
		g2d.fill(new Rectangle(5, 145, 85, 5));
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(new Rectangle(scroll, 140, 5, 15));

		g2d.setColor(Color.WHITE);
		// an interesting visual representation of the mouse coordinate
		int xgrey = (int) (255 * ((float) getMouseX() / getWidth()));
		if (xgrey > 255)
			xgrey = 255;
		if (xgrey < 0)
			xgrey = 0;
		g2d.setColor(new Color(xgrey, xgrey, xgrey));
		g2d.fill(new Rectangle(5, 160, 40, 40));
		int ygrey = (int) (255 * ((float) getMouseY() / getHeight()));
		if (ygrey > 255)
			ygrey = 255;
		if (ygrey < 0)
			ygrey = 0;
		g2d.setColor(new Color(ygrey, ygrey, ygrey));
		g2d.fill(new Rectangle(50, 160, 40, 40));

		g2d.setColor(Color.BLACK);
		g2d.drawString("LP", 13, 31);// Left pressed
		g2d.drawString("RP", 56, 31);// Right pressed
		g2d.drawString("LJ", 13, 76);// Left just pressed
		g2d.drawString("RJ", 56, 76);// Right just pressed
		g2d.drawString("LR", 13, 121);// Left released
		g2d.drawString("RR", 56, 121);// Right released
		g2d.drawString("X", 18, 188);// Left released
		g2d.drawString("Y", 62, 188);// Right released

		// Key stuff

		g2d.setFont(new Font("sansserif", Font.PLAIN, 15));
		g2d.setColor(keyPressed() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(135, 5, 80, 20));
		g2d.setColor(Color.BLACK);
		g2d.drawString("Key Down", 140, 20);

		g2d.setColor(capsUsed() ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(135, 65, 80, 20));
		g2d.setColor(Color.BLACK);
		g2d.drawString("Caps", 157, 80);

		g2d.setColor(Color.WHITE);
		g2d.drawString("Chars Down: " + charsDown(), 135, 42);
		g2d.drawString("Key-codes Down: " + keyCodesDown(), 135, 57);

		// arrow key stuff

		g2d.setColor(keyCodesDown().contains(KeyEvent.VK_UP) ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(getWidth() - 90, getHeight() - 110, 40, 40));

		g2d.setColor(keyCodesDown().contains(KeyEvent.VK_DOWN) ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(getWidth() - 90, getHeight() - 65, 40, 40));

		g2d.setColor(keyCodesDown().contains(KeyEvent.VK_LEFT) ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(getWidth() - 135, getHeight() - 65, 40, 40));

		g2d.setColor(keyCodesDown().contains(KeyEvent.VK_RIGHT) ? Color.GREEN : Color.RED);
		g2d.fill(new Rectangle(getWidth() - 45, getHeight() - 65, 40, 40));

		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString("Game-Loop Indicator",
				getWidth() / 3 - g2d.getFontMetrics().stringWidth("Game-Loop Indicator") / 2, getHeight()-8);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(new Rectangle(getFrameCount() % (getWidth() + 5) - 5, getHeight() - 20, 5, 15));
	}
}
