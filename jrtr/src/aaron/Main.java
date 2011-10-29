package aaron;

import jrtr.Shape;
import aaron.demos.Demo;
import aaron.shapes.Cylinder;

public class Main {
	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 */
	public static void main(String[] args) {
		Demo demo = new Demo();
		demo.display();
		
	}
}