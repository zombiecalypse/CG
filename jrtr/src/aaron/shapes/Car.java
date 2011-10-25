package aaron.shapes;

import javax.vecmath.Matrix4f;

import aaron.colorizations.Color;

public class Car extends ComplexShape {
	private ComplexShape frontAxis;
	private ComplexShape backAxis;

	public Car() {
		genAxes();
		genChasis();
	}

	class Axis extends ComplexShape {
		private Matrix4f rotation;

		public Axis() {
			rotation = new Matrix4f();
			rotation.setIdentity();
			rotation.rotY(0.01f);
			add(new Cylinder(0.1f, 2.8f, new Color(0.1f, 0.1f, 0.1f)))
					.zOrientation(0, 1, 0).done();
			add(new Torus(0.3f, 1, new Color(0.1f, 0.1f, 0.1f)))
					.at(0, 1.25f, 0).zOrientation(0, 1, 0).done();
			add(new Torus(0.3f, 1, new Color(0.1f, 0.1f, 0.1f)))
					.at(0, -1.25f, 0).zOrientation(0, -1, 0).done();
		}

		public void update() {
			Matrix4f newTrans = new Matrix4f(this.transformation);
			newTrans.mul(rotation);
			this.setTransformation(newTrans);
		}
	}

	private ComplexShape createAxis() {
		return new Axis();
	}

	private void genAxes() {
		this.frontAxis = createAxis();
		this.backAxis = createAxis();
		add(frontAxis).at(1.25f, 0, 0).done();
		add(backAxis).at(-1.25f, 0, 0).done();
	}

	private void genChasis() {
		add(new Cylinder(1.3f, 4)).at(0, 0, 1).zOrientation(1, 0, 0).done();
		add(new Cylinder(1.3f, 1.3f, new Color(0.1f, 0.1f, 0.5f))).at(0, 0, 2)
				.done();
	}
}
