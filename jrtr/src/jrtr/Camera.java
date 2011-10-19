package jrtr;

import javax.vecmath.*;
import static aaron.Helpers.*;

/**
 * Stores the specification of a virtual camera. You will extend
 * this class to construct a 4x4 camera matrix, i.e., the world-to-
 * camera transform from intuitive parameters. 
 * 
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a camera.
 */
public class Camera {

	private Matrix4f cameraMatrix;
	private Vector3f centerOfProjection;
	private Vector3f lookAt;
	private Vector3f up;
	private boolean cacheValid;
	
	/**
	 * Construct a camera with a default camera matrix. The camera
	 * matrix corresponds to the world-to-camera transform. This default
	 * matrix places the camera at (0,0,10) in world space, facing towards
	 * the origin (0,0,0) of world space, i.e., towards the negative z-axis.
	 */
	public Camera()	{
		this(new Vector3f(5,5,5), new Vector3f(0,0,0), Z);
	}
	
	public Camera(Vector3f centerOfProjection, Vector3f lookAt, Vector3f up) {
		this.centerOfProjection = centerOfProjection;
		this.lookAt = lookAt;
		this.up = up;
		this.cameraMatrix = new Matrix4f();
		this.cacheValid = false;
	}
	
	/**
	 * Return the camera matrix, i.e., the world-to-camera transform. For example, 
	 * this is used by the renderer.
	 * 
	 * @return the 4x4 world-to-camera transform matrix
	 */
	public Matrix4f getCameraMatrix() {
		if (!cacheValid) updateCache();
		Matrix4f m = new Matrix4f();
		m.set(cameraMatrix);
		return m;
	}

	private void updateCache() {
		Matrix4f translation = getTranslation();
		Matrix4f lookAtMat = lookAtMatrix();
		Matrix4f upMat = upMatrix();
		this.cameraMatrix.setIdentity();
		this.cameraMatrix.mul(this.cameraMatrix, lookAtMat);
		this.cameraMatrix.mul(this.cameraMatrix, upMat);
		this.cameraMatrix.mul(translation);
		this.cameraMatrix.m33 = 0;
		this.cameraMatrix.m32 = 1;
	}

	private Matrix4f getTranslation() {
		Matrix4f translation = new Matrix4f();
		translation.setIdentity();
		translation.setTranslation(centerOfProjection);
		return translation;
	}
	
	private Matrix4f upMatrix() {
		Vector3f upDir = new Vector3f();
		upDir.set(this.up);
		upDir.sub(this.centerOfProjection);
		upDir.normalize();
		AxisAngle4f up = new AxisAngle4f();
		Vector3f upAxis = new Vector3f();
		upAxis.cross(upDir, Z);
		float zAngle = (float) Math.acos(upDir.dot(Z));
		up.set(upAxis, zAngle);
		Matrix4f upMat = new Matrix4f();
		upMat.setIdentity();
		upMat.set(up);
		return upMat;
	}
	private Matrix4f lookAtMatrix() {
		Vector3f lookDir = new Vector3f();
		lookDir.set(this.lookAt);
		lookDir.sub(this.centerOfProjection);
		lookDir.normalize();
		AxisAngle4f look = new AxisAngle4f();
		Vector3f lookAxis = new Vector3f();
		lookAxis.cross(lookDir, X);
		float xAngle = (float) Math.acos(lookDir.dot(X));
		look.set(lookAxis, xAngle);
		Matrix4f lookMat = new Matrix4f();
		lookMat.setIdentity();
		lookMat.set(look);
		return lookMat;
	}
}
