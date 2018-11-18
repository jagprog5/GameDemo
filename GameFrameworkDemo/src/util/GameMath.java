package util;

public class GameMath {
	private GameMath() {}
	
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	public static float[] projectileSpeed(float shooterX, float shooterY, float targetX, float targetY, float projectileSpeed) {
		//good old grade 10 similar triangles
		float zVal = projectileSpeed / distance(shooterX, shooterY, targetX, targetY);
		return new float[] {zVal * (shooterX - targetX), zVal * (shooterY - targetY)};
	}
}
