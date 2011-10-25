package aaron;

import java.util.Timer;

import jrtr.GLRenderPanel;
import jrtr.RenderContext;
import jrtr.SWRenderPanel;
import aaron.demos.Demo;

/**
 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to provide
 * a call-back function for initialization.
 */
public final class SimpleRenderPanel extends GLRenderPanel {
	private Demo parent;
	public SimpleRenderPanel(Demo parent) {
		this.parent = parent;
	}
	/**
	 * Initialization call-back. We initialize our renderer here.
	 * 
	 * @param r
	 *            the render context that is associated with this render
	 *            panel
	 */
	public void init(RenderContext r) {
		r.setSceneManager(parent.getSceneManager());

		// Register a timer task
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(parent.animationTask(), 0, 10);
	}
}