package aaron.shapes;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

public interface Normalizator {
	public Vector3f normal(int row, int col, Tuple3f pos);
}
