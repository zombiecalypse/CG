package aaron.demos;

import java.util.Collection;
import java.util.HashSet;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.vecmath.Matrix4f;

import aaron.SimpleRenderPanel;
import aaron.VirtualTrackball;
import aaron.WSListener;
import aaron.shapes.Car;
import aaron.shapes.ComplexShape;
import aaron.shapes.IShape;
import jrtr.Camera;
import jrtr.SceneManagerInterface;
import jrtr.SimpleSceneManager;

public class Demo {
	private Collection<IShape> shapes;
	
	/**
	 * A timer task that generates an animation. This task triggers the
	 * redrawing of the 3D scene every time it is executed.
	 */
	public class AnimationTask extends TimerTask {
		public void run() {
			for (IShape shape : shapes) {
				shape.update();
			}
			Demo.this.update();
			// Trigger redrawing of the render window
			renderPanel.getCanvas().repaint();
		}
	}
	
	private Camera camera;
	private SimpleSceneManager scenemanager;
	private SimpleRenderPanel renderPanel;

	public Demo() {
		this.camera = new Camera();
		this.scenemanager = new SimpleSceneManager();
		this.renderPanel = new SimpleRenderPanel(this);
		this.shapes = new HashSet<IShape>();
		
		createScene();
	}
	
	public void update() {
		// TODO Auto-generated method stub
		
	}

	private void createScene() {
		ComplexShape shape = new Car();
		Matrix4f position = new Matrix4f();
		position.setIdentity();
		scenemanager.addShape(shape, position);
		shapes.add(shape);
	}

	public void display() {
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas
																// into a JFrame
																// window

		// Add a mouse listener
		VirtualTrackball listener = new VirtualTrackball(camera);
		WSListener ws = new WSListener(camera);
		renderPanel.getCanvas().addMouseMotionListener(listener);
		jframe.addMouseListener(listener);
		jframe.addKeyListener(ws);
		renderPanel.getCanvas().addKeyListener(ws);

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}

	public SceneManagerInterface getSceneManager() {
		return this.scenemanager;
	}

	public TimerTask animationTask() {
		return new AnimationTask();
	}
}
