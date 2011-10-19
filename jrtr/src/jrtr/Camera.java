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
		Vector3f g = new Vector3f(lookAt);
		g.sub(new Vector3f(centerOfProjection));
		g.normalize();
		Vector3f w = new Vector3f(up);
		w.normalize();
		Vector3f v = new Vector3f();
		v.cross(g,w);
		v.normalize();
		Matrix4f ortho = new Matrix4f();
		ortho.setIdentity();
		ortho.setRow(0,new Vector4f(g));
		ortho.setRow(1,new Vector4f(w));
		ortho.setRow(2,new Vector4f(v));
		ortho.setRow(3,new Vector4f(centerOfProjection));
		ortho.invert();
		cameraMatrix = ortho;
	}
}
