package aaron;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;

import jrtr.Camera;

/**
 * A mouse listener for the main window of this application. This can be used to
 * process mouse events.
 */
public class VirtualTrackball implements GLEventListener, MouseMotionListener,
		MouseListener {
	private Integer startX, startY;
	private Camera camera;

	public VirtualTrackball(Camera camera) {
		this.camera = camera;
	}

	public synchronized void mouseDragged(MouseEvent e) {
		int endX = e.getX();
		int endY = e.getY();
		if (startX != null && startY != null) {
			int dx = startX - endX;
			int dy = startY - endY;
			AxisAngle4f xTurn = new AxisAngle4f(0, 0, 1,
					(float) (dx * Math.PI / 250));
			AxisAngle4f yTurn = new AxisAngle4f(1, 0, 0,
					(float) (dy * Math.PI / 250));
			Matrix4f xTurnMat = new Matrix4f();
			xTurnMat.set(xTurn);
			Matrix4f yTurnMat = new Matrix4f();
			yTurnMat.set(yTurn);
			Matrix4f turnMat = new Matrix4f();
			turnMat.mul(yTurnMat, xTurnMat);
			camera.transform(turnMat);
		}
		startX = endX;
		startY = endY;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
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

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}