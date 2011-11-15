package jrtr;

import javax.vecmath.*;

import aaron.shapes.IShape;

/**
 * Represents a 3D shape. The shape currently just consists of its vertex data.
 * It should later be extended to include material properties, shaders, etc.
 */
public class Shape implements IShape {

	public Material material;

	/**
	 * Make a shape from {@link VertexData}.
	 * 
	 * @param vertexData
	 *            the vertices of the shape.
	 */
	public Shape(VertexData vertexData) {
		this();
		this.vertexData = vertexData;
	}

	public Shape() {
		t = new Matrix4f();
		t.setIdentity();
		this.material = new Material();
	}

	public VertexData getVertexData() {
		assert vertexData != null : "Not fully initialized";
		return vertexData;
	}

	public void setTransformation(Matrix4f t) {
		this.t = t;
	}

	public Matrix4f getTransformation() {
		return t;
	}

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Material getMaterial() {
		return this.material;
	}

	protected VertexData vertexData;
	private Matrix4f t;

	@Override
	public void update() {
	}
}
