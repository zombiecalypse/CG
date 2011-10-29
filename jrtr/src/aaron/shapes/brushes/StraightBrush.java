package aaron.shapes.brushes;

import java.util.Iterator;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class StraightBrush implements Iterator<Vector3f>, Iterable<Vector3f> {

	private Point3f start;
	private Point3f towards;
	private int steps;
	private int at;
	private Vector3f step;

	public StraightBrush(Point3f start, Point3f towards, int steps) {
		this.start = start;
		this.towards = towards;
		this.step = new Vector3f();
		this.step.sub(towards, start);
		this.step.scale(1.0f/(steps-1));
		this.steps = steps;
		this.at = 0;
	}
	@Override
	public Iterator<Vector3f> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return at < steps;
	}

	@Override
	public Vector3f next() {
		Vector3f there = new Vector3f(step);
		there.scaleAdd(at, start);
		at++;
		return there;
	}

	@Override
	public void remove() {	}

}
