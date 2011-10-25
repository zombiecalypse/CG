package jrtr;

import javax.vecmath.*;
import static aaron.Helpers.*;

/**
 * Stores the specification of a virtual camera. You will extend this class to
 * construct a 4x4 camera matrix, i.e., the world-to- camera transform from
 * intuitive parameters.
 * 
 * A scene manager (see {@link SceneManagerInterface},
 * {@link SimpleSceneManager}) stores a camera.
 */
public class Camera {

	private Matrix4f cameraMatrix;
	private Point3f centerOfProjection;
	private Vector3f lookAt;
	private Vector3f up;
	private boolean cacheValid;

	public Point3f getCenterOfProjection() {
		return centerOfProjection;
	}

	public void setCenterOfProjection(Point3f centerOfProjection) {
		this.centerOfProjection = centerOfProjection;
		cacheValid = false;
	}

	public Vector3f getLookAt() {
		return lookAt;
	}

	public void setLookAt(Vector3f lookAt) {
		this.lookAt = lookAt;
		cacheValid = false;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
		cacheValid = false;
	}

	public void transform(Matrix4f mat) {
		mat.transform(this.centerOfProjection);
		mat.transform(lookAt);
		mat.transform(up);
		cacheValid = false;
	}

	public Vector3f lookAtDir() {
		Vector3f dir = new Vector3f(lookAt);
		dir.sub(centerOfProjection);
		cacheValid = false;
		return dir;
	}

	public void pointAt(Vector3f v) {
		Vector3f axis = new Vector3f();
		Vector3f g = new Vector3f(v);
		g.sub(this.centerOfProjection);
		axis.cross(lookAtDir(), g);
		AxisAngle4f rot = new AxisAngle4f(axis, g.dot(lookAtDir()));
		Matrix4f rotMat = new Matrix4f();
		rotMat.set(rot);
		transform(rotMat);
	}

	/**
	 * Construct a camera with a default camera matrix. The camera matrix
	 * corresponds to the world-to-camera transform. This default matrix places
	 * the camera at (0,0,10) in world space, facing towards the origin (0,0,0)
	 * of world space, i.e., towards the negative z-axis.
	 */
	public Camera() {
		this(new Point3f(0, 5, 0), Y, Z);
		// this(new Vector3f(5,5,5), new Vector3f(0,0,0), Z);
	}

	public Camera(Point3f centerOfProjection, Vector3f lookAt, Vector3f up) {
		this.centerOfProjection = centerOfProjection;
		this.lookAt = lookAt;
		this.up = up;
		this.cameraMatrix = new Matrix4f();
		this.cacheValid = false;
	}

	/**
	 * Return the camera matrix, i.e., the world-to-camera transform. For
	 * example, this is used by the renderer.
	 * 
	 * @return the 4x4 world-to-camera transform matrix
	 */
	public Matrix4f getCameraMatrix() {
		if (!cacheValid)
			updateCache();
		Matrix4f m = new Matrix4f();
		m.set(cameraMatrix);
		return m;
	}

	private void updateCache() {
		Vector3f w = new Vector3f(lookAt);
		w.normalize();
		Vector3f u = new Vector3f();
		u.cross(w, up);
		u.normalize();
		Vector3f v = new Vector3f();
		v.cross(u, w);
		v.normalize();
		Matrix4f ortho = new Matrix4f();
		ortho.setIdentity();
		ortho.setColumn(0, new Vector4f(u));
		ortho.setColumn(1, new Vector4f(v));
		ortho.setColumn(2, new Vector4f(w));
		ortho.setColumn(3, new Vector4f(centerOfProjection));
		ortho.m33 = 1;
		ortho.invert();
		cameraMatrix = ortho;
	}
}
