package aaron.shapes;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

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
	static ComplexShape shape;
	static float angle;
	private static Matrix4f positionOfCar;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. 
	 */ 
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
	
			// Register a timer task
		    Timer timer = new Timer();
		    angle = 0.0f;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}

	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask
	{
		public void run()
		{
			// Update transformation
    		Matrix4f t = new Matrix4f();
    		t.setIdentity();
    		Matrix4f rotZ = new Matrix4f();
    		rotZ.rotZ(angle+0.005f);
    		t.mul(rotZ);
    		shape.update();
    		sceneManager.transformShape(shape, t);
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
    	public void mousePressed(MouseEvent e) {}
    	public void mouseReleased(MouseEvent e) {}
    	public void mouseEntered(MouseEvent e) {}
    	public void mouseExited(MouseEvent e) {}
    	public void mouseClicked(MouseEvent e) {}
	}
	
	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 */
	public static void main(String[] args)
	{		
		
				
		Matrix4f appealing = new Matrix4f();
		Matrix4f rotX = new Matrix4f();
		Matrix4f rotY = new Matrix4f();
		rotX.rotX(-1.0f);
		rotY.rotY(0.1f);
		appealing.mul(rotX,rotY);
		appealing.setTranslation(new Vector3f(0,0,-30));
		// Make a scene manager and add the object
		sceneManager = new SimpleSceneManager();
		sceneManager.getCamera().setCameraMatrix(appealing);
		shape = new Car();
		positionOfCar = new Matrix4f();
		positionOfCar.setIdentity();
		AxisAngle4f rot = new AxisAngle4f(0,0,1,1.5707963f);
		positionOfCar.set(rot);
		positionOfCar.setTranslation( new Vector3f(10,0,0));
		sceneManager.addShape(shape, positionOfCar);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
	    jframe.addMouseListener(new SimpleMouseListener());
		   	    	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}