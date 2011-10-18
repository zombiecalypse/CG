package aaron.colorizations;

import java.util.Random;

import javax.vecmath.Vector3f;


/**
 * Colours by using two separate colourizers that colour different 
 * sides of a plane.
 * @author aaron
 *
 */
public class LimitColorization extends Colorization {
	
	private Colorization low;
	private Colorization high;
	private Vector3f limit;
	private Random random;

	public LimitColorization(Colorization low, Colorization high, Vector3f limit) {
		this.low = low;
		this.high = high;
		this.limit = limit;
		this.random = new Random();
	}
	
	public float flux() {
		return this.random.nextFloat()*0.2f + 0.9f;
	}

	@Override
	public Vector3f color(int x, int y, Vector3f position) {
		if (limit.dot(position)*flux() > 1) {
			return this.high.color(x, y, position);
		}
		else {
			return this.low.color(x, y, position);
		}
	}

}
