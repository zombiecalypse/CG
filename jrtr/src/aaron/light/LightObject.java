package aaron.light;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;


public class LightObject extends Light{
	protected Vector4f position;
	protected Vector4f direction;
	private Matrix4f transform;
	protected float exponent;

	public LightObject(Vector4f radiance) {
		super(radiance);
		this.transform = new Matrix4f();
		this.transform.setIdentity();
	}

	@Override
	public void setTransformation(Matrix4f t) {
		this.transform = t;
		
	}

	@Override
	public Matrix4f getTransformation() {
		return this.transform;
	}

	@Override
	public void update() {		
	}

	@Override
	public LightData getData() {
		LightData data = new LightData();
		data.specular = this.radiance;
		data.diffuse = this.radiance;
		data.position = this.position;
		this.transform.transform(data.position);
		
		return data;
	}

}
