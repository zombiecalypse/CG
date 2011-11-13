package aaron.light;

import javax.vecmath.Vector4f;

import aaron.shapes.IShape;

/**
 * Stores the properties of a light source. To be implemented for the
 * "Texturing and Shading" project.
 */
public abstract class Light implements IShape {
	protected Vector4f radiance;

	public Light(Vector4f radiance) {
		this.radiance = radiance;
	}
	
	public abstract LightData getData();
	public class LightData {
		public Vector4f 
			ambient,
			diffuse,
			specular,
			position,
			spot_direction;
		public float
			spot_exponent,
			spot_cutoff;
		public LightData() {
			ambient = new Vector4f();
			diffuse = new Vector4f();
			specular = new Vector4f();
			position = new Vector4f();
			spot_direction = new Vector4f();
			spot_exponent = 0;
			spot_cutoff = 0;
		}
		public LightData ambient(float r, float g, float b, float a) {
			ambient = new Vector4f(r,g,b,a);
			return this;
		}
		
		public LightData diffuse(float r, float g, float b, float a) {
			diffuse = new Vector4f(r,g,b,a);
			return this;
		}
		
		public LightData specular(float r, float g, float b, float a) {
			specular = new Vector4f(r,g,b,a);
			return this;
		}
		
		public LightData position(float r, float g, float b, float a) {
			position = new Vector4f(r,g,b,a);
			return this;
		}
		
		public LightData spotDirection(float r, float g, float b, float a) {
			spot_direction = new Vector4f(r,g,b,a);
			return this;
		}
		
		public LightData spotExponent(float r) {
			spot_exponent = r;
			return this;
		}
		
		public LightData spotCutoff(float r) {
			spot_cutoff = r;
			return this;
		}
	}
}
