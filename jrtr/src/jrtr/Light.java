package jrtr;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import aaron.shapes.IShape;

/**
 * Stores the properties of a light source. To be implemented for the
 * "Texturing and Shading" project.
 */
public abstract class Light implements IShape {
	private float radiance;
	private Point3f position;

	public Light(float radiance) {
		this.radiance = radiance;
	}
	
	public float shineAt(Point3f p) {
		Vector3f d = new Vector3f();
		d.sub(position, p);
		float distanceSquared = d.lengthSquared();
		d.normalize();
		return strength(d)/distanceSquared;
	}
	
	protected abstract float strength(Vector3f direction);
}
