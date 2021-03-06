package aaron.demos;

import java.util.Collection;
import java.util.HashSet;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

import aaron.light.RadialLight;
import aaron.shapes.Car;
import aaron.shapes.ComplexShape;
import aaron.shapes.IShape;
import aaron.ui.CameraObject;
import aaron.ui.SimpleRenderPanel;
import aaron.ui.VirtualTrackball;
import aaron.ui.WSListener;
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
	
	private CameraObject camera;
	private SimpleSceneManager scenemanager;
	private SimpleRenderPanel renderPanel;
	private VirtualTrackball listener;
	private WSListener ws;

	public Demo() {
		this.scenemanager = new SimpleSceneManager();
		this.camera = scenemanager.getCamera();
		this.renderPanel = new SimpleRenderPanel(this);
		this.shapes = new HashSet<IShape>();
		this.listener = new VirtualTrackball(camera);
		this.ws = new WSListener(camera);
		
		createScene();
	}
	
	public void update() {
	}
	
	public void addShape(IShape shape, Matrix4f position) {
		this.shapes.add(shape);
		scenemanager.addShape(shape, position);
	}

	protected void createScene() {	
		this.scenemanager.addLight(new RadialLight(new Vector4f(1,1,1,1), new Vector4f(0,0,4,1)));
	}

	public void display() {
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas
																// into a JFrame
																// window

		// Add a mouse listener

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
