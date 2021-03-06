package jrtr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.vecmath.Matrix4f;

import aaron.light.Light;
import aaron.shapes.ComplexShape;
import aaron.shapes.IShape;
import aaron.ui.CameraObject;

/**
 * A simple scene manager that stores objects in a linked list.
 */
public class SimpleSceneManager implements SceneManagerInterface {

	private Map<Shape, Matrix4f> shapes;
	private CameraObject camera;
	private Frustum frustum;
	private Set<Light> lightSources;

	public SimpleSceneManager() {
		this(new CameraObject(), new Frustum());
	}

	public SimpleSceneManager(CameraObject c) {
		this(c, new Frustum());
	}

	public SimpleSceneManager(CameraObject c, Frustum f) {
		camera = c;
		frustum = f;
		shapes = new HashMap<Shape, Matrix4f>();
		lightSources = new HashSet<Light>();
	}

	public CameraObject getCamera() {
		return camera;
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public void addShape(Shape shape, Matrix4f t) {
		shapes.put(shape, t);
	}

	public void addShape(IShape shape) {
		Matrix4f id = new Matrix4f();
		id.setIdentity();
		addShape(shape, id);
	}

	public void addShape(ComplexShape shape, Matrix4f t) {
		for (IShape ri : shape.getShapes()) {
			addShape(ri, t);
		}
	}

	public void addShape(IShape shape, Matrix4f t) {
		if (shape instanceof Shape) {
			addShape((Shape) shape, t);
		} else if (shape instanceof ComplexShape) {
			addShape((ComplexShape) shape, t);
		}
	}

	public void transformShape(IShape shape, Matrix4f t) {
		if (shape instanceof Shape) {
			transformShape((Shape) shape, t);
		} else if (shape instanceof ComplexShape) {
			transformShape((ComplexShape) shape, t);
		}
	}

	public void transformShape(Shape shape, Matrix4f m) {
		Matrix4f res = new Matrix4f();
		Matrix4f A = this.shapes.get(shape);
		res.mul(m, A);
		shapes.put(shape, res);
	}

	public void transformShape(ComplexShape shape, Matrix4f t) {
		for (IShape ri : shape.getShapes()) {
			transformShape(ri, t);
		}
	}

	public SceneManagerIterator iterator() {
		return new SimpleSceneManagerItr(this);
	}

	
	public void addLight(Light light) {
		this.lightSources.add(light);
	}
	public void removeLight(Light light) {
		this.lightSources.remove(light);
	}
	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Iterator<Light> lightIterator() {
		return this.lightSources.iterator();
	}
	
	public Iterable<Light> getLights() {
		return this.lightSources;
	}

	private class SimpleSceneManagerItr implements SceneManagerIterator {

		private ListIterator<Entry<Shape, Matrix4f>> itr;

		public SimpleSceneManagerItr(SimpleSceneManager sceneManager) {
			this.itr = new LinkedList<Entry<Shape, Matrix4f>>(
					sceneManager.shapes.entrySet()).listIterator();
		}

		public boolean hasNext() {
			return itr.hasNext();
		}

		public RenderItem next() {
			Entry<Shape, Matrix4f> pos = itr.next();
			Shape shape = pos.getKey();
			Matrix4f t = new Matrix4f();
			t.mul(pos.getValue(), shape.getTransformation());
			return new RenderItem(shape, t);
		}
	}
}
