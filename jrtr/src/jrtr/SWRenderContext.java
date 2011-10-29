package jrtr;

import jrtr.RenderContext;
import jrtr.SWRenderContext.Point;
import jrtr.SWRenderContext.Triangle;
import jrtr.VertexData.VertexElement;

import java.awt.image.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * A skeleton for a software renderer. It works in combination with
 * {@link SWRenderPanel}, which displays the output image. In project 3 you will
 * implement your own rasterizer in this class.
 * <p>
 * To use the software renderer, you will simply replace {@link GLRenderPanel}
 * with {@link SWRenderPanel} in the user application.
 */
public class SWRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private BufferedImage colorBuffer;
	private float[][] zBuffer;
	private ArrayList<Point> points;
	private ArrayList<Triangle> triangles;

	public void setSceneManager(SceneManagerInterface sceneManager) {
		this.sceneManager = sceneManager;
	}

	/**
	 * This is called by the SWRenderPanel to render the scene to the software
	 * frame buffer.
	 */
	public void display() {

		if (points == null ) 
			points = new ArrayList<Point>();
		if (triangles == null) 
			triangles = new ArrayList<Triangle>();
		if (sceneManager == null)
			return;

		beginFrame();

		SceneManagerIterator iterator = sceneManager.iterator();
		while (iterator.hasNext()) {
			draw(iterator.next());
		}

		endFrame();
	}

	/**
	 * This is called by the {@link SWJPanel} to obtain the color buffer that
	 * will be displayed.
	 */
	public BufferedImage getColorBuffer() {
		return colorBuffer;
	}

	/**
	 * Set a new viewport size. The render context will also need to store a
	 * viewport matrix, which you need to reset here.
	 */
	public void setViewportSize(int width, int height) {
		colorBuffer = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
	}

	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame() {
		this.zBuffer = new float[colorBuffer.getWidth()][colorBuffer.getHeight()];
		this.points.clear();
		this.triangles.clear();
	}

	private void endFrame() {
	}

	/**
	 * The main rendering method. You will need to implement this to draw 3D
	 * objects.
	 */
	private void draw(RenderItem renderItem) {
		Shape shape = renderItem.getShape();
		
		Matrix4f t = new Matrix4f();
		t.set(sceneManager.getCamera().getCameraMatrix());
		t.mul(renderItem.getT());
		
		VertexData vertexData = shape.getVertexData();
		
		LinkedList<VertexElement> vertexElements = vertexData
				.getElements();
		int indices[] = vertexData.getIndices();

		if (indices == null)
			throw new IllegalArgumentException("Have no indeces");
		
		for (VertexElement element : vertexElements) {
			switch (element.getSemantic()) {
			case POSITION:
				preparePosition(element);
			case NORMAL:
				prepareNormal(element);
			case COLOR:
				prepareColor(element);
			case TEXCOORD:
				prepareTexture(element);
			}
		}
		buildTriags(vertexData.getIndices());
		reallyDrawStuff(t);
	}
	
	private void buildTriags(int[] indices) {
		assert indices.length % 3 == 0 : "Indices of triangles are not sane " + indices.length;
		for (int i = 0; i < indices.length ; i+= 3) {
			Triangle triangle = new Triangle();
			triangles.add(triangle);
			int ip = indices[i], iq = indices[i+1], iu = indices[i+2];
			assert points.get(ip) != null : "No index "+ip;
			assert points.get(iq) != null : "No index "+iq;
			assert points.get(iu) != null : "No index "+iu;
			triangle.p = points.get(ip);
			triangle.q = points.get(iq);
			triangle.u = points.get(iu);
		}
	}

	/// works with prepared triags.
	private void reallyDrawStuff(Matrix4f t) {
		for (Triangle triag : triangles) {
		}
	}

	private void prepareTexture(VertexElement element) {
		int dim = element.getNumberOfComponents();
		assert dim == 2 : "Weird texture coordinates with "+dim+ " dimensions";
		float data[] = element.getData();
		for (int i = 0; i < data.length; i+=dim) {
			Point p = points.get(i/dim);
			p.textureCoord = new Vector2f(data[i],data[i+1]);
		}
	}

	private void prepareColor(VertexElement element) {
		int dim = element.getNumberOfComponents();
		assert dim == 3 : "Weird Color space: "+dim;
		float data[] = element.getData();
		
		for (int i = 0; i < data.length; i+= dim) {
			Point p = points.get(i/dim);
			p.color = new Vector3f(data[i], data[i+1], data[i+2]);
		}
	}

	private void prepareNormal(VertexElement element) {
		int dim = element.getNumberOfComponents();
		assert dim == 3 : "Weird dimension: "+dim;
		float data[] = element.getData();
		
		for (int i = 0; i < data.length; i+= dim) {
			Point p = points.get(i/dim);
			p.normal = new Vector3f(data[i], data[i+1], data[i+2]);
		}
	}

	private void preparePosition(VertexElement element) {
		int dim = element.getNumberOfComponents();
		assert dim == 3 : "Weird dimension: "+dim;
		float data[] = element.getData();		
		for (int i = 0 ; i < data.length; i+=dim) {
			Point p = new Point();
			points.add(p);
			p.position = new Vector3f(data[i], data[i+1], data[i+2]);
		}
	}

	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public Shader makeShader() {
		return new SWShader();
	}

	/**
	 * Does nothing. We will not implement textures for the software renderer.
	 */
	public Texture makeTexture() {
		return new SWTexture();
	}
	
	class Point {
		public Vector3f position;
		public Vector3f color;
		public Vector3f normal;
		public Vector2f textureCoord;
	}
	
	class Triangle {
		public Point p,q,u;
	}
}
