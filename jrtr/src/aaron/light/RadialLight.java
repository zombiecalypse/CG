package aaron.light;

import javax.vecmath.Vector4f;

public class RadialLight extends LightObject {

	public RadialLight(Vector4f radiance, Vector4f position) {
		super(radiance);
		this.position = position;
	}

}
