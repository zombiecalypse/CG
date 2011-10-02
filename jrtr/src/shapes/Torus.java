package shapes;


import java.util.Random;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import jrtr.Shape;
import jrtr.VertexData;


public class Torus extends Shape {
	public Torus(float r, float R) {
		super(grid(r,R, 40,40, new Vector3f(0.75f,0,0)));
	}

	private static VertexData grid(float r, float R, 
			int stepsAroundSmallCircle, 
			int stepsAroundBigCircle, Vector3f color) {
		Random colorVariation = new Random();
		Grid points = new Grid(stepsAroundBigCircle, stepsAroundSmallCircle);
		int tickLarge = 0;
		Vector3f orientation = new Vector3f(0,0,1);
		for (Vector3f radius : new CircleBrush(R, stepsAroundBigCircle, new Vector3f(0,0,0), orientation)) {
			int tickSmall = 0;
			Vector3f smallLoopDirection = new Vector3f();
			smallLoopDirection.cross(orientation, radius);
			for (Vector3f torusPiece : new CircleBrush(r, stepsAroundSmallCircle, radius, smallLoopDirection)) {
				Vector3f variedColor = new Vector3f();
				variedColor.set(color);
				variedColor.add(new Vector3f(colorVariation.nextFloat()*0.4f,colorVariation.nextFloat()*0.4f,colorVariation.nextFloat()*0.4f));
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
