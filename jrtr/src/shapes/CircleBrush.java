package shapes;

import java.util.Iterator;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class CircleBrush implements Iterator<Vector3f>, Iterable<Vector3f> {

	private static final float TwoPI = 6.2831853f; // Mmmmh, two pies.
	public static final Vector3f Z = new Vector3f(0,0,1);
	private Vector3f center;
	private int upto;
	private float radius;
	private int index;
	private AxisAngle4f rotation;
	private float step;
	private Vector3f axis;
	private Matrix3f secondaryRotationMatrix;

	public CircleBrush(float radius, int steps, Vector3f center) {
		this(radius, steps, center, Z);
	}
	public CircleBrush(float radius, int steps) {
		this(radius, steps, new Vector3f(0,0,0));
	}
	public CircleBrush(float radius, int steps, Vector3f center, Vector3f orientation) {
		assert ! orientation.epsilonEquals(new Vector3f(0,0,0), 0.1f);
		this.center = center;
		this.axis = orientation;
		this.axis.normalize();
		this.rotation = new AxisAngle4f(0,0,1, 0);
		this.upto = steps;
		this.step = TwoPI /steps;
		this.radius = radius;
		this.index = 0;
		

		Vector3f secondaryAxis = new Vector3f();
		secondaryAxis.cross(Z, axis);
		AxisAngle4f secondaryRotation = new AxisAngle4f(secondaryAxis,(float) Math.acos(Z.dot(axis)));
		this.secondaryRotationMatrix = new Matrix3f();
		secondaryRotationMatrix.set(secondaryRotation);
	}
	@Override
	public boolean hasNext() {
		return this.index < this.upto;
	}

	@Override
	public Vector3f next() { 
		Vector3f here = new Vector3f(1,0,0);
		here.scale(radius);
		
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.setIdentity();
		rotationMatrix.set(this.rotation);
		rotationMatrix.transform(here);
		secondaryRotationMatrix.transform(here);
		here.add(this.center);
		this.rotation.angle += this.step; 
		this.index++;
		return here;
	}

	@Override
	public void remove() {	}
	
	@Override
	public Iterator<Vector3f> iterator() {
		return this;
	}

}
