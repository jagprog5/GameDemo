package Assets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.GameMath;

public class Images {
	private Images() {
	}

	public static final String PLASMA = "Plasma_Ball.png";
	public static final String BOSS = "Boss_Center.png";
	public static final String CHARGINGBOSS = "Charging_Boss_Center.png";
	public static final String CHARGINGPLASMA = "Charging_Plasma_Ball.png";
	//1 in 1000 chance of displaying default skin. Easter egg.
	public static final boolean DEFAULT = (int) (GameMath.random.nextFloat() * 1000) == 0;

	public static final Image getImage(String name) {
		if (!DEFAULT) {
		try {
			Image img = ImageIO.read(Images.class.getResourceAsStream(name));
			return img;
		} catch (java.lang.IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Reasource \"" + name + "\" was not found!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Assets had trouble loading an image. \n" + e.getMessage());
		}
		}
		BufferedImage bi = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, 1, 1);
		g.setColor(Color.GREEN);
		g.fillRect(0, 1, 1, 1);
		g.setColor(Color.BLUE);
		g.fillRect(1, 0, 1, 1);
		g.setColor(Color.WHITE);
		g.fillRect(1, 1, 1, 1);
		return bi;
	}
}
