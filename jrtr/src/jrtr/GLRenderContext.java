package jrtr;

import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.vecmath.*;

import aaron.Helpers;
import aaron.light.Light;
import aaron.light.TooManyLightsException;
import aaron.light.Light.LightData;

/**
 * This class implements a {@link RenderContext} (a renderer) using OpenGL
 * version 3 (or later).
 */
public class GLRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private GL3 gl;
	private GLShader activeShader;
	private Material material;

	/**
	 * This constructor is called by {@link GLRenderPanel}.
	 * 
	 * @param drawable
	 *            the OpenGL rendering context. All OpenGL calls are directed to
	 *            this object.
	 */
	public GLRenderContext(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL3();
		gl.glEnable(GL3.GL_DEPTH_TEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Load and use default shader
		GLShader defaultShader = new GLShader(gl);
		try {
			defaultShader.load(Helpers.getHardwareShader(),
					Helpers.getHardwareShaderFrag());
		} catch (Exception e) {
			System.out.print("Problem with shader:\n");
			System.out.print(e.getMessage());
		}
		defaultShader.use();
		activeShader = defaultShader;
	}

	/**
	 * Set the scene manager. The scene manager contains the 3D scene that will
	 * be rendered. The scene includes geometry as well as the camera and
	 * viewing frustum.
	 */
	public void setSceneManager(SceneManagerInterface sceneManager) {
		this.sceneManager = sceneManager;
	}

	/**
	 * This method is called by the GLRenderPanel to redraw the 3D scene. The
	 * method traverses the scene using the scene manager and passes each object
	 * to the rendering method.
	 */
	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL3();

		beginFrame();

		SceneManagerIterator iterator = sceneManager.iterator();
		while (iterator.hasNext()) {
			RenderItem r = iterator.next();
			if (r.getShape() != null)
				draw(r);
		}

		endFrame();
	}

	/**
	 * This method is called at the beginning of each frame, i.e., before scene
	 * drawing starts.
	 */
	private void beginFrame() {
		try {
			setLights();
		} catch (TooManyLightsException e) {
		}

		gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL3.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * This method is called at the end of each frame, i.e., after scene drawing
	 * is complete.
	 */
	private void endFrame() {
		gl.glFlush();
	}

	/**
	 * Convert a Matrix4f to a float array in column major ordering, as used by
	 * OpenGL.
	 */
	private float[] matrix4fToFloat16(Matrix4f m) {
		float[] f = new float[16];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				f[j * 4 + i] = m.getElement(i, j);
		return f;
	}

	/**
	 * The main rendering method.
	 * 
	 * @param renderItem
	 *            the object that needs to be drawn
	 */
	private void draw(RenderItem renderItem) {
		VertexData vertexData = renderItem.getShape().getVertexData();
		LinkedList<VertexData.VertexElement> vertexElements = vertexData
				.getElements();
		int indices[] = vertexData.getIndices();

		// Don't draw if there are no indices
		if (indices == null)
			return;

		// Set the material
		setMaterial(renderItem.getShape().getMaterial());

		// Compute the modelview matrix by multiplying the camera matrix and the
		// transformation matrix of the object

		Matrix4f t = new Matrix4f();
		t.set(sceneManager.getCamera().getCameraMatrix());
		t.mul(renderItem.getT());

		// Set modelview and projection matrices in shader
		gl.glUniformMatrix4fv(
				gl.glGetUniformLocation(activeShader.programId(), "modelview"),
				1, false, matrix4fToFloat16(t), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(),
				"projection"), 1, false, matrix4fToFloat16(sceneManager
				.getFrustum().getProjectionMatrix()), 0);

		// Steps to pass vertex data to OpenGL:
		// 1. For all vertex attributes (position, normal, etc.)
		// Copy vertex data into float buffers that can be passed to OpenGL
		// Make/bind vertex buffer objects
		// Tell OpenGL which "in" variable in the shader corresponds to the
		// current attribute
		// 2. Render vertex buffer objects
		// 3. Clean up
		// Note: Of course it would be more efficient to store the vertex buffer
		// objects (VBOs) in a
		// vertex array object (VAO), and simply bind ("load") the VAO each time
		// to render. But this
		// requires a bit more logic in the rendering engine, so we render every
		// time "from scratch".

		// 1. For all vertex attributes, make vertex buffer objects
		IntBuffer vboBuffer = IntBuffer.allocate(vertexElements.size());
		gl.glGenBuffers(vertexElements.size(), vboBuffer);
		ListIterator<VertexData.VertexElement> itr = vertexElements
				.listIterator(0);
		while (itr.hasNext()) {
			// Copy vertex data into float buffer
			VertexData.VertexElement e = itr.next();
			int dim = e.getNumberOfComponents();

			FloatBuffer varr = FloatBuffer.allocate(indices.length * dim);
			for (int i = 0; i < indices.length; i++) {
				for (int j = 0; j < dim; j++) {
					varr.put(e.getData()[dim * indices[i] + j]);
				}
			}

			// Make vertex buffer object
			gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboBuffer.get());
			// Upload vertex data
			gl.glBufferData(GL3.GL_ARRAY_BUFFER, varr.array().length * 4,
					FloatBuffer.wrap(varr.array()), GL3.GL_DYNAMIC_DRAW);

			// Tell OpenGL which "in" variable in the vertex shader corresponds
			// to the current vertex buffer object
			// We use our own convention to name the variables, i.e.,
			// "position", "normal", "color", "texcoord", or others if necessary
			int attribIndex = -1;
			if (e.getSemantic() == VertexData.Semantic.POSITION) {
				attribIndex = gl.glGetAttribLocation(activeShader.programId(),
						"position");
			} else if (e.getSemantic() == VertexData.Semantic.NORMAL) {
				attribIndex = gl.glGetAttribLocation(activeShader.programId(),
						"normal");
			} else if (e.getSemantic() == VertexData.Semantic.COLOR) {
				attribIndex = gl.glGetAttribLocation(activeShader.programId(),
						"color");
			} else if (e.getSemantic() == VertexData.Semantic.TEXCOORD) {
				attribIndex = gl.glGetAttribLocation(activeShader.programId(),
						"texcoord");
			}
			gl.glVertexAttribPointer(attribIndex, dim, GL3.GL_FLOAT, false, 0,
					0);
			gl.glEnableVertexAttribArray(attribIndex);
		}

		// 3. Render the vertex buffer objects
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, indices.length);

		// 4. Clean up
		gl.glDeleteBuffers(vboBuffer.array().length, vboBuffer.array(), 0);

		cleanMaterial(renderItem.getShape().getMaterial());
	}

	/**
	 * Pass the material properties to OpenGL, including textures and shaders.
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	private void setMaterial(Material m) {
		GL2 gl2 = gl.getGL2();
		if (m.hasTexture()) {
			Texture tex = m.getTexture();
			gl.glBindTexture(GL.GL_TEXTURE_2D, tex.getId());
		}
		
		activeShader = m.getShader();
		gl2.glMaterialfv(GL.GL_FRONT_AND_BACK,GLLightingFunc.GL_AMBIENT_AND_DIFFUSE , toBuffer(m.getDiffuse()));
		gl2.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_SPECULAR, toBuffer(m.getSpecular()));
		gl2.glMaterialf(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_SHININESS, m.getShininess());
	}
	
	static private FloatBuffer toBuffer(Vector4f v) {
		FloatBuffer buffer = FloatBuffer.allocate(4);
		buffer.put(v.x);
		buffer.put(v.y);
		buffer.put(v.z);
		buffer.put(v.w);
		return buffer;
	}

	/**
	 * Pass the light properties to OpenGL. This assumes the list of lights in
	 * the scene manager is accessible via a method Iterator<Light>
	 * lightIterator().
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 * @throws TooManyLightsException 
	 */
	void setLights() throws TooManyLightsException {
		GL2 gl2 =gl.getGL2(); 
		int lights[] = {GLLightingFunc.GL_LIGHT0,GLLightingFunc.GL_LIGHT1,GLLightingFunc.GL_LIGHT2,GLLightingFunc.GL_LIGHT3,
				GLLightingFunc.GL_LIGHT4,GLLightingFunc.GL_LIGHT5,GLLightingFunc.GL_LIGHT6,GLLightingFunc.GL_LIGHT7};
		int i = 0;
		for (Light light : this.sceneManager.getLights()) {
			if (i > 7)
				throw new TooManyLightsException("No support for this many light sources");
			gl2.glEnable(lights[i]);
			LightData lightData = light.getData();
			FloatBuffer buffer = FloatBuffer.allocate(8*4*17);
			writeToBuffer(buffer,lightData.ambient);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_AMBIENT, buffer);
			writeToBuffer(buffer,lightData.diffuse);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_DIFFUSE, buffer);
			writeToBuffer(buffer,lightData.position);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_POSITION, buffer);
			writeToBuffer(buffer,lightData.specular);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_SPECULAR, buffer);
			writeToBuffer(buffer,lightData.spot_direction);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_SPOT_DIRECTION, buffer);
			writeToBuffer(buffer,lightData.spot_cutoff);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_SPOT_CUTOFF, buffer);
			writeToBuffer(buffer,lightData.spot_exponent);
			gl2.glLightfv(lights[i], GLLightingFunc.GL_SPOT_EXPONENT, buffer);
			i++;
		}
	}
	
	private void writeToBuffer(FloatBuffer buffer, float val) {
		buffer.put(val);
	}

	private void writeToBuffer(FloatBuffer arr, Vector4f v) {
		arr.put(v.x);
		arr.put(v.y);
		arr.put(v.z);
		arr.put(v.w);
	}

	/**
	 * Disable a material.
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	private void cleanMaterial(Material m) {
	}

	public Shader makeShader() {
		return new GLShader(gl);
	}

	public Texture makeTexture() {
		return new GLTexture(gl);
	}
}
