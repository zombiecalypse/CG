package aaron.colorizations;

import java.util.Random;

import javax.vecmath.Vector3f;

public class Color extends Colorization {
	
	public static final Color RED = new Color(new Vector3f(1,0,0));
	public static final Color BLUE = new Color(new Vector3f(0,0,1));
	public static final Color GREEN = new Color(new Vector3f(0,1,0));
	
	public static final Color BLACK = new Color(new Vector3f(0,0,0));
	public static final Color WHITE = new Color(new Vector3f(1,1,1));
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
	public Vector3f color(int x, int y, Vector3f _) {
		Vector3f variedColor = new Vector3f();
		variedColor.set(baseColor);
		variedColor.add(new Vector3f(colorVariation.nextFloat()*irregularity,colorVariation.nextFloat()*irregularity,colorVariation.nextFloat()*irregularity));
		return variedColor;
	}
	
}
