package shapes;

import java.util.Iterator;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class CircleBrush implements Iterator<Vector4f>, Iterable<Vector4f> {

	private static final float TwoPI = 6.2831853f; // Mmmmh, two pies.
	private Vector4f center;
	private int upto;
	private float radius;
	private int index;
	private AxisAngle4f rotation;
	private float step;

	public CircleBrush(float radius, int steps, Vector4f center, Vector3f orientation) {
		this.center = center;
		this.rotation = new AxisAngle4f(orientation.x, orientation.y, orientation.z, 0);
		this.upto = steps;
		this.step = TwoPI /steps;
		this.radius = radius;
		this.index = 0;
	}
	@Override
	public boolean hasNext() {
		return this.index < this.upto;
	}

	@Override
	public Vector4f next() { 
		Vector4f here = new Vector4f(1,0,0,0);
		Matrix4f rotationMatrix = new Matrix4f();
		this.rotation.angle += this.step; 
		rotationMatrix.setRotation(this.rotation);
		rotationMatrix.transform(here);
		here.add(this.center);
		return here;
	}

	@Override
	public void remove() {	}
	
	@Override
	public Iterator<Vector4f> iterator() {
		return this;
	}

}
