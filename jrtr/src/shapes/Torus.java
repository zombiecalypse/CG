package shapes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import jrtr.Shape;
import jrtr.VertexData;

public class Torus extends Shape {
	public Torus(float r, float R) {
		super(grid(r,R, 5,10));
	}

	private static VertexData grid(float r, float R, 
			int stepsAroundSmallCircle, 
			int stepsAroundBigCircle) {
		Grid points = new Grid(stepsAroundBigCircle, stepsAroundSmallCircle);
		int tickLarge = 0;
		Vector3f orientation = new Vector3f(0,0,1);
		for (Vector4f radius : new CircleBrush(R, stepsAroundBigCircle, new Vector4f(0,0,0,1), orientation)) {
			int tickSmall = 0;
			Vector3f smallLoopDirection = new Vector3f();
			smallLoopDirection.cross(orientation, new Vector3f(radius.x, radius.y, radius.z));
			for (Vector4f torusPiece : new CircleBrush(r, stepsAroundSmallCircle, radius, smallLoopDirection)) {
				points.set(tickLarge, tickSmall).to(torusPiece);
				points.connect(tickLarge, tickSmall).toNeighbors();
			}
		}
		points
			.glueX()
			.glueY();
	}
	
}
