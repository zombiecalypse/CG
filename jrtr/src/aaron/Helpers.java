package aaron;

import javax.vecmath.Vector3f;

public class Helpers {
	public final static Vector3f X = new Vector3f(1, 0, 0);
	public final static Vector3f Y = new Vector3f(0, 1, 0);
	public final static Vector3f Z = new Vector3f(0, 0, 1);
	public final static Vector3f N = new Vector3f(0, 0, 0);
	public final static String shadersPath = "../jrtr/shaders/";
	
	public enum Shader {
		DEFAULT {
			@Override
			String vertPath() {
				return "default.vert";
			}

			@Override
			String fragPath() {
				return "default.frag";
			}
		}, NORMAL {
			@Override
			String vertPath() {
				return "normal.vert";
			}

			@Override
			String fragPath() {
				return "default.frag";
			}
		}, TOON {

			@Override
			String vertPath() {
				return "toon.vert";
			}

			@Override
			String fragPath() {
				return "toon.frag";
			}
		}, DIFFUSE {

			@Override
			String vertPath() {
				return "diffuse.vert";
			}

			@Override
			String fragPath() {
				return "diffuse.frag";
			}
			
		}, PHONG {

			@Override
			String vertPath() {
				return "phong.vert";
			}

			@Override
			String fragPath() {
				return "phong.frag";
			}
			
		}, TEXTURE {

			@Override
			String vertPath() {
				return "texture.vert";
			}

			@Override
			String fragPath() {
				return "texture.frag";
			}
			
		}, WIREFRAME {

			@Override
			String vertPath() {
				return "wireframe.vert";
			}

			@Override
			String fragPath() {
				return "wireframe.frag";
			}
			
		}
		;
		abstract String vertPath();
		abstract String fragPath();
	}
	
	public static Shader shader = Shader.NORMAL;
	
	public static void setShader(Shader s) {
		shader = s;
	}
	
	public static String getHardwareShader() {
		return shadersPath + shader.vertPath();
	}
	public static String getHardwareShaderFrag() {
		return shadersPath + shader.fragPath();
	}
}
