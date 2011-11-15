package aaron.shapes;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import aaron.shapes.ComplexShape.Adder;

import jrtr.Material;
import jrtr.RenderItem;
import jrtr.Shape;
import jrtr.VertexData;

/**
 * A shape composed of other shapes.
 * 
 * @author Aaron
 * 
 */
public class ComplexShape implements IShape {
	private static final Vector3f Z = new Vector3f(0, 0, 1);
	private static final Vector3f X = new Vector3f(1, 0, 0);
	private List<IShape> subshapes;
	protected Matrix4f transformation;
	private Material material;

	public ComplexShape() {
		subshapes = new ArrayList<IShape>();
		transformation = new Matrix4f();
		transformation.setIdentity();
	}

	public List<IShape> getShapes() {
		return new ArrayList<IShape>(subshapes);
	}

	public Adder add(IShape s) {
		return new Adder(s);
	}

	class Adder {
		private IShape shape;
		private Vector3f position;
		private Vector3f zOrient;
		private Vector3f xOrient;

		public Adder(IShape s) {
			this.shape = s;
			zOrient = new Vector3f(0, 0, 1);
			xOrient = new Vector3f(1, 0, 0);
			position = new Vector3f();
		}

		public Adder at(Vector3f v) {
			this.position = v;
			return this;
		}

		public Adder zOrientation(Vector3f v) {
			zOrient = v;
			return this;
		}

		public Adder xOrientation(Vector3f v) {
			xOrient = v;
			return this;
		}

		private Matrix4f getZTransform() {
			Matrix4f zTransform = new Matrix4f();
			zTransform.setIdentity();
			AxisAngle4f zTurn = new AxisAngle4f();
			float zAngle = (float) Math.acos(zOrient.dot(Z));
			Vector3f axis = new Vector3f();
			axis.cross(zOrient, Z);
			zTurn.set(axis, zAngle);
			zTransform.setRotation(zTurn);
			return zTransform;
		}

		private Matrix4f getXTransform() {
			Matrix4f xTransform = new Matrix4f();
			xTransform.setIdentity();
			AxisAngle4f xTurn = new AxisAngle4f();
			float xAngle = (float) Math.acos(xOrient.dot(X));
			Vector3f axis = new Vector3f();
			axis.cross(xOrient, X);
			xTurn.set(axis, xAngle);
			xTransform.setRotation(xTurn);
			return xTransform;
		}

		public ComplexShape done() {
			Matrix4f zTransform = getZTransform();
			Matrix4f xTransform = getXTransform();
			zTransform.mul(xTransform);
			zTransform.setTranslation(position);
			zTransform.mul(ComplexShape.this.transformation);
			shape.setTransformation(zTransform);
			ComplexShape.this.subshapes.add(shape);
			return ComplexShape.this;
		}

		public Adder at(float i, float j, float k) {
			return at(new Vector3f(i, j, k));
		}

		public Adder zOrientation(float i, float j, float k) {
			return zOrientation(new Vector3f(i, j, k));
		}

		public Adder xOrientation(float i, float j, float k) {
			return xOrientation(new Vector3f(i, j, k));
		}
	}

	public Matrix4f getTransformation() {
		return transformation;
	}

	public void setTransformation(Matrix4f t) {
		transformation.invert();
		Matrix4f inverted = transformation;
		for (IShape s : subshapes) {
			Matrix4f sTrans = new Matrix4f();
			sTrans.mul(inverted, s.getTransformation());
			sTrans.mul(t, sTrans);
			s.setTransformation(sTrans);
		}
		transformation = t;
	}

	@Override
	public void update() {
		for (IShape s : subshapes)
			s.update();
	}

	public Material getMaterial() {
		return this.material;
	}

	public void setMaterial(Material material) {
		this.material =material;
	}
}
