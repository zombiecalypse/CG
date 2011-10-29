package aaron;

import javax.vecmath.Vector3f;

public class Helpers {
	public final static Vector3f X = new Vector3f(1, 0, 0);
	public final static Vector3f Y = new Vector3f(0, 1, 0);
	public final static Vector3f Z = new Vector3f(0, 0, 1);
	public final static Vector3f N = new Vector3f(0, 0, 0);
	
	enum Shader {
		DEFAULT, NORMAL
	}
	
	public final static Shader shader = Shader.NORMAL;
	
	public static String getHardwareShader() {
		switch (shader) {
		case DEFAULT:
			return "../jrtr/shaders/default.vert";
		case NORMAL:
			return "../jrtr/shaders/normal.vert";
		}
		return null;
	}
	public static String getHardwareShaderFrag() {
		switch (shader) {
		case DEFAULT:
			return "../jrtr/shaders/default.frag";
		case NORMAL:
			return "../jrtr/shaders/normal.frag";
		}
		return null;
	}
}
