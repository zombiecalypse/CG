package aaron.shapes;


import java.util.Random;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import aaron.Color;

import jrtr.Shape;
import jrtr.VertexData;


public class Torus extends Shape {
	public Torus(float r, float R) {
		this(r,R, Color.RED);
	}
	
	public Torus(float r, float R, Color color) {
		super(grid(r,R, 40,40, color));
	}

	private static VertexData grid(float r, float R, 
			int stepsAroundSmallCircle, 
			int stepsAroundBigCircle, Color color) {
		Grid points = new Grid(stepsAroundBigCircle, stepsAroundSmallCircle);
		int tickLarge = 0;
		Vector3f orientation = new Vector3f(0,0,1);
		for (Vector3f radius : new CircleBrush(R, stepsAroundBigCircle, new Vector3f(0,0,0), orientation)) {
			int tickSmall = 0;
			Vector3f smallLoopDirection = new Vector3f();
			smallLoopDirection.cross(orientation, radius);
			for (Vector3f torusPiece : new CircleBrush(r, stepsAroundSmallCircle, radius, smallLoopDirection)) {
				Vector3f variedColor =  color.color(tickLarge,tickSmall);
				points.set(tickLarge, tickSmall)
					.to(torusPiece)
					.in(variedColor);
				tickSmall++;
			}
			tickLarge++;
		}
		points
			.connectNeighbors()
			.glueX()
			.glueY();
		return points.vertexData();
	}
	
}
