package aaron;

import javax.vecmath.Matrix4f;

import jrtr.Camera;

public class CameraObject extends Camera {
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
}
