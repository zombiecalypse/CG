package jrtr;

import javax.vecmath.Matrix4f;

/**
 * Stores the specification of a viewing frustum, or a viewing
 * volume. The viewing frustum is represented by a 4x4 projection
 * matrix. You will extend this class to construct the projection 
 * matrix from intuitive parameters.
 * <p>
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a frustum.
 */
public class Frustum {

	private Matrix4f projectionMatrix;
	
	/**
	 * Construct a default viewing frustum. The frustum is given by a 
	 * default 4x4 projection matrix.
	 */
	public Frustum() {
		this(1f, 1.0471976f, 0.001f,100f);
	}
	public Frustum(float aspect, float theta, float n, float f) {
		float t = (float) (Math.tan(theta/2)*n);
		float r = t*aspect;
		projectionMatrix = new Matrix4f();
		float a[] = {n/r, 0.f, 0.f, 0.f, 
					 0.f, n/t, 0.f, 0.f,
				     0.f, 0.f, -(f+n)/(f-n), 2*f*n/(n-f),
				     0.f, 0.f, -1.f, 0.f};
		projectionMatrix.set(a);
	}
	
	/**
	 * Return the 4x4 projection matrix, which is used for example by 
	 * the renderer.
	 * 
	 * @return the 4x4 projection matrix
	 */
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
}
