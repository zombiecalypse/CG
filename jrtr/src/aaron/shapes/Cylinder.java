package aaron.shapes;

import java.util.Random;

import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import aaron.colorizations.Color;
import aaron.shapes.brushes.CircleBrush;
import aaron.shapes.brushes.StraightBrush;

import jrtr.Shape;
import jrtr.VertexData;
import static aaron.Helpers.*;

public class Cylinder extends Shape {

	public Cylinder(float r, float h, Color color) {
		super(grid(r, h, 40, color));
	}

	public Cylinder(float r, float h) {
		this(r, h, Color.RED);
	}

	private static VertexData grid(float r, float h, int stepsInCircle,
			Color color) {
		Grid grid = new Grid(stepsInCircle, 52);
		grid.setNormal(new Normalizator() {
			@Override
			public Vector3f normal(int row, int col, Tuple3f pos) {
				if (row == 51) return new Vector3f(Z);
				if (row == 0) {
					Vector3f v = new Vector3f(Z);
					v.negate();
					return v;
				}
				Vector3f v = new Vector3f(pos);
				v.z = 0;
				return v;
			}
		});
		int i = 1;
		int circleIndex = 0;
		for (Vector3f circle : new CircleBrush(r, stepsInCircle, new Vector3f(0,0,-h/2))) {
			Vector3f variedColor = color.color(circleIndex, 0);
			grid.set(circleIndex,0).to(new Vector3f(0,0,-h/2)).in(variedColor);
			circleIndex++;
		}
		circleIndex = 0;
		for (Vector3f circle : new CircleBrush(r, stepsInCircle, new Vector3f(0,0,h/2))) {
			Vector3f variedColor = color.color(circleIndex, 0);
			grid.set(circleIndex,51).to(new Vector3f(0,0,h/2)).in(variedColor);
			circleIndex++;
		}
		for (Vector3f hull : new StraightBrush(new Point3f(0,0,-h/2), new Point3f(0,0,h/2), 50)) {
			circleIndex = 0;
			for (Vector3f circle : new CircleBrush(r, stepsInCircle, hull)) {
				Vector3f variedColor = color.color(circleIndex, i);
				grid.set(circleIndex, i).to(circle).in(variedColor);
//				grid.connect(0, i).to(circleIndex, i);
				circleIndex++;
			}
			i++;
		}
		grid.connectNeighbors().glueX().glueY();
		return grid.vertexData();
	}

}
