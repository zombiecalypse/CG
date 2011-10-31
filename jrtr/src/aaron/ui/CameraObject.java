package aaron.ui;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import aaron.shapes.IShape;

import jrtr.Camera;

public class CameraObject extends Camera implements IShape {

	public CameraObject() {
	}

//	public Matrix4f getCameraMatrix() {
//		Matrix4f m = new Matrix4f(super.getCameraMatrix());
//		return m;
//	}

	@Override
	public void setTransformation(Matrix4f t) {
		Matrix4f m = new Matrix4f(this.getCameraMatrix());
		m.invert();
		m.mul(t,m);
		Point3f center = this.getCenterOfProjection();
		Vector3f up = this.getUp();
		Vector3f lookAt = this.getLookAt();
		m.transform(center);
		m.transform(up);
		m.transform(lookAt);
		this.setCenterOfProjection(center);
		this.setUp(up);
		this.setLookAt(lookAt);
	}

	@Override
	public Matrix4f getTransformation() {
		return getCameraMatrix();
	}

	@Override
	public void update() {	}
}
