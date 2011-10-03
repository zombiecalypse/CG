package aaron;

import java.util.Random;

import javax.vecmath.Vector3f;

public class Color implements IColor {
	
	public static final Color RED = new Color(new Vector3f(1,0,0));
	private Random colorVariation;
	private Vector3f baseColor;
	private float irregularity;

	public Color(Vector3f base) {
		this.colorVariation = new Random();
		this.baseColor = base;
		this.irregularity = 0.4f;
	}
	
	public Color(float r, float g, float b) {
		this(new Vector3f(r,g,b));
	}

	@Override
	public Vector3f color(int x, int y) {
		Vector3f variedColor = new Vector3f();
		variedColor.set(baseColor);
		variedColor.add(new Vector3f(colorVariation.nextFloat()*irregularity,colorVariation.nextFloat()*irregularity,colorVariation.nextFloat()*irregularity));
		return variedColor;
	}
	
}
