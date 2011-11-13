package aaron.light;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

public class AmbientLight extends Light {

	public AmbientLight(Vector4f radiance) {
		super(radiance);
	}

	@Override
	public void setTransformation(Matrix4f t) {
	}

	@Override
	public Matrix4f getTransformation() {
		Matrix4f t = new Matrix4f();
		t.setIdentity();
		return t;
	}

	@Override
	public void update() {
	}

	@Override
	public LightData getData() {
		return new LightData().ambient(radiance.x, radiance.y, radiance.z, radiance.w);
	}

}
