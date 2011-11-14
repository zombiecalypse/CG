package jrtr;

import java.nio.FloatBuffer;

import javax.vecmath.Vector4f;

import aaron.colorizations.Color;

/**
 * Stores the properties of a material. You will implement this class in the
 * "Shading and Texturing" project.
 */
public class Material {
	private GLShader shader;
	private Texture texture;
	private Color color;
	private Vector4f diffuse;
	private Vector4f specular;
	private float shininess;
	
	public Material(GLShader shader) {
		this();
		this.shader = shader;
	}
	
	public Material(GLShader shader, Texture texture) {
		this(shader);
		this.texture = texture;
	}
	
	public Material(GLShader shader, Color color) {
		this(shader);
		this.color = color;
	}

	public Material() {
		this.diffuse = new Vector4f();
		this.specular = new Vector4f();
		this.shininess = 0;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public GLShader getShader() {
		return shader;
	}

	public boolean hasTexture() {
		return texture == null;
	}

	public Vector4f getDiffuse() {
		return diffuse;
	}

	public Vector4f getSpecular() {
		return this.specular;
	}

	public float getShininess() {
		return this.shininess;
	}
}
