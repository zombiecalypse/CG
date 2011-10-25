package aaron.shapes;

import javax.vecmath.Matrix4f;

public interface IShape {
	public void setTransformation(Matrix4f t);

	public Matrix4f getTransformation();

	void update();
}
