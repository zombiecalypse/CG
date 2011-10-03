package shapes;

import java.util.Random;

import javax.vecmath.Vector3f;

import jrtr.Shape;
import jrtr.VertexData;

public class Cylinder extends Shape {

	public Cylinder(float r, float h, Vector3f color) {
		super(grid(r,h,40,color));
	}
	
	public Cylinder(float r, float h) {
		this(r,h,new Vector3f(1,0,0));
	}

	private static VertexData grid(float r, float h, int stepsInCircle, Vector3f color) {
		Grid grid = new Grid(stepsInCircle, 2);
		Random colorVariation = new Random();
		for (int i = 0; i < 2; i++) {
			int circleIndex = 0;
			Vector3f middle = new Vector3f(0,0,i*h-h/2);
			for (Vector3f circle : new CircleBrush(r, stepsInCircle, middle)) {
				Vector3f variedColor = new Vector3f();
				variedColor.set(color);
				variedColor.add(new Vector3f(colorVariation.nextFloat()*0.4f,colorVariation.nextFloat()*0.4f,colorVariation.nextFloat()*0.4f));
				grid.set(circleIndex,i)
					.to(circle)
					.in(variedColor);
				grid.connect(0, i).to(circleIndex, i);
				circleIndex++;
			}
		}
		grid
			.connectNeighbors()
			.glueX()
			.glueY();
		return grid.vertexData();
	}

}
