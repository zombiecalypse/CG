package aaron.ui;

import javax.vecmath.Matrix4f;

import aaron.shapes.IShape;

import jrtr.Camera;

public class CameraObject extends Camera implements IShape {
	private Matrix4f mat;

	public CameraObject() {
		mat = new Matrix4f();
		mat.setIdentity();
	}

	public Matrix4f getCameraMatrix() {
		Matrix4f m = new Matrix4f(super.getCameraMatrix());
		m.mul(mat, m);
		return m;
	}

	@Override
	public void setTransformation(Matrix4f t) {
		mat = t;
	}

	@Override
	public Matrix4f getTransformation() {
		return mat;
	}

	@Override
	public void update() {	}
}
