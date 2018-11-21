package Assets;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	private Images() {}
	
	public static final String FIREBALL = "FireBall.png";
	public static final String FACE = "Face.png";
	public static final String FACEHURT = "FaceHurt.png";
	public static final String HOTFIREBALL = "HotFireBall.png";
	
	public static final Image getImage(String name) {
		try {
			Image img = ImageIO.read(Images.class.getResourceAsStream(name));
			return img;
		} catch (java.lang.IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("Reasource \"" + name + "\" was not found!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Assets had trouble loading an image. \n" + e.getMessage());
		}
	}
}
