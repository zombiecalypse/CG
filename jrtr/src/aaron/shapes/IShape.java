package aaron.shapes;

import javax.vecmath.Matrix4f;

import jrtr.Material;

public interface IShape {
	public void setTransformation(Matrix4f t);

	public Matrix4f getTransformation();

	void update();

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public void setMaterial(Material material);

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Material getMaterial();
}
