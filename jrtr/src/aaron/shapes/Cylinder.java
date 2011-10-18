package aaron.shapes;

import java.util.Random;

import javax.vecmath.Vector3f;

import aaron.colorizations.Color;

import jrtr.Shape;
import jrtr.VertexData;

public class Cylinder extends Shape {

	public Cylinder(float r, float h, Color color) {
		super(grid(r,h,40,color));
	}
	
	public Cylinder(float r, float h) {
		this(r,h,Color.RED);
	}

	private static VertexData grid(float r, float h, int stepsInCircle, Color color) {
		Grid grid = new Grid(stepsInCircle, 2);
		for (int i = 0; i < 2; i++) {
			int circleIndex = 0;
			Vector3f middle = new Vector3f(0,0,i*h-h/2);
			for (Vector3f circle : new CircleBrush(r, stepsInCircle, middle)) {
				Vector3f variedColor = color.color(circleIndex,i);
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
