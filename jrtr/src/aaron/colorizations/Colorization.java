package aaron.colorizations;

import javax.vecmath.Vector3f;

public abstract class Colorization {
	public abstract Vector3f color(int x, int y, Vector3f position);
	
	public Vector3f color(int x, int y) {
		return color(x,y,null);
	}
}
