package aaron;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Camera;

public class WSListener implements GLEventListener, KeyListener {
	private Camera camera;

	public WSListener(Camera camera) {
		this.camera = camera;
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Point3f cop = new Point3f(camera.getCenterOfProjection());
		Vector3f dir = new Vector3f(camera.getLookAt());
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