package aaron.light;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

public class SpotLight extends LightObject {
	private Vector4f direction;
	private float exponent;

	public SpotLight(Vector4f radiance, Vector4f position, Vector4f direction) {
		super(radiance);
		this.position = position;
		this.direction = direction;
	}
	
	public void setExponent(float s) {
		this.exponent = s;
	}


	@Override
	public void update() {
	}

	@Override
	public LightData getData() {
		Vector4f dir = new Vector4f(this.direction);
		this.getTransformation().transform(dir);
		return super.getData()
				.spotCutoff((float) (Math.PI/2))
				.spotDirection(dir.x, dir.y, dir.z, dir.w)
				.spotExponent(this.exponent);
	}

}
