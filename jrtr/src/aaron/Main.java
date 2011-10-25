package aaron;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import aaron.shapes.Car;
import aaron.shapes.ComplexShape;
import aaron.shapes.IShape;
import aaron.shapes.SimpleLandscape;

import jrtr.Camera;
import jrtr.GLRenderPanel;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.SWRenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;
import jrtr.VertexData;

public class Main {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static IShape shape;
	static float angle;
	private static Matrix4f positionOfCar;
	private static CameraObject camera;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to provide
	 * a call-back function for initialization.
	 */
	public final static class SimpleRenderPanel extends GLRenderPanel {
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r
		 *            the render context that is associated with this render
		 *            panel
		 */
		public void init(RenderContext r) {
			renderContext = r;
			renderContext.setSceneManager(sceneManager);

			// Register a timer task
			Timer timer = new Timer();
			angle = 0.0f;
			timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}

	/**
	 * A timer task that generates an animation. This task triggers the
	 * redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask {
		public void run() {
			// Update transformation
			Matrix4f t = new Matrix4f();
			t.setIdentity();
			Matrix4f rotZ = new Matrix4f();
			rotZ.rotZ(angle + 0.005f);
			t.mul(rotZ);
			shape.update();

			// Trigger redrawing of the render window
			renderPanel.getCanvas().repaint();
		}
	}

	public static class WSListener implements GLEventListener, KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Point3f cop = new Point3f(camera.getCenterOfProjection());
			Vector3f dir = new Vector3f(camera.getLookAt());
			System.out.println(cop);
			System.out.println(dir);
			if (e.getKeyChar() == 'w') {
				dir.scale(-0.125f);
				cop.add(dir);
			} else if (e.getKeyChar() == 's') {
				dir.scale(0.125f);
				cop.add(dir);
			} else if (e.getKeyChar() == 'd') {
				dir.scale(0.125f);
				dir.cross(dir, camera.getUp());
				cop.add(dir);
			} else if (e.getKeyChar() == 'a') {
				dir.scale(-0.125f);
				dir.cross(dir, camera.getUp());
				cop.add(dir);
			}
			System.out.println(cop);
			System.out.println();
			camera.setCenterOfProjection(cop);

		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void display(GLAutoDrawable arg0) {
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
		}

		@Override
		public void init(GLAutoDrawable arg0) {
		}

		@Override
		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
				int arg4) {
		}
	}

	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 */
	public static void main(String[] args) {

		camera = new CameraObject();
		// Make a scene manager and add the object
		sceneManager = new SimpleSceneManager(camera);
		showCar();

		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas
																// into a JFrame
																// window

		// Add a mouse listener
		VirtualTrackball listener = new VirtualTrackball(camera);
		WSListener ws = new WSListener();
		renderPanel.getCanvas().addMouseMotionListener(listener);
		jframe.addKeyListener(ws);
		renderPanel.getCanvas().addKeyListener(ws);

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}

	private static void showCar() {
		Matrix4f appealing = new Matrix4f();
		appealing.setIdentity();

		shape = new SimpleLandscape();
		positionOfCar = new Matrix4f();
		positionOfCar.setIdentity();
		sceneManager.addShape(shape, positionOfCar);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
	}
}