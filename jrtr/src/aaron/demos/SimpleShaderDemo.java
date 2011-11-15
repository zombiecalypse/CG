package aaron.demos;

import javax.vecmath.Matrix4f;

import aaron.Helpers;
import aaron.Helpers.Shader;
import aaron.shapes.Car;

public class SimpleShaderDemo extends Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Helpers.setShader(Shader.DIFFUSE);
		SimpleShaderDemo self = new SimpleShaderDemo();
		self.display();
	}
	
	protected void createScene() {
		super.createScene();
		Matrix4f t = new Matrix4f();
		t.setIdentity();
		this.addShape(new Car(), t);
	}

}
