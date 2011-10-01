package shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector4f;

import jrtr.VertexData;

/**
 * A Builder for VertexData that consists of a 2d grid.
 * @author Aaron
 *
 */
public class Grid {
	private Vector4f[][] points;
	private Map<Vector4f,List<Vector4f>> connections;
	private int cols;
	private int rows;

	public Grid(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.points = new Vector4f[rows][cols];
		this.connections = new HashMap<Vector4f, List<Vector4f>>();
	}

	public Grid glueX() {
		for (int y = 0; y < this.rows; y++) {
			this.connect(0,y).to(this.cols-1,y);
		}
		return this;
	}
	
	public Grid glueY() {
		for (int x = 0; x < this.cols; x++) {
			this.connect(x,0).to(x,this.rows-1);
		}
		return this;
	}
	
	public VertexData vertexData() {
		VertexData data = new VertexData(cols*rows);
		float positionsArray[] = new float[cols*rows*3];
		int index = 0;
		for (Vector4f[] row : points) 
			for (Vector4f point : row) {
				positionsArray[index++] = point.x;
				positionsArray[index++] = point.y;
				positionsArray[index++] = point.z;
			}
		data.addElement(positionsArray, VertexData.Semantic.POSITION, 3);
		// TODO Connections
	}

	public Connector connect(int x, int y) {
		return new Connector(this, x, y);
	}

	public Setter set(int x, int y) {
		return new Setter(x, y);
	}
	
	class Setter {
		private int x;
		private int y;

		public Setter(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void to(Vector4f v) {
			Grid.this.points[x][y] = v;
		}
	}
	class Connector {
		private Vector4f here;
		private int x;
		private int y;

		public Connector(Grid grid, int x, int y) {
			this.here = grid.points[x][y];
			this.x = x;
			this.y = y;
		}
		
		public void to(int x, int y) {
			if (!Grid.this.connections.containsKey(this.here))
				Grid.this.connections.put(this.here, new ArrayList<Vector4f>());
			Grid.this.connections.get(this.here).add(Grid.this.points[x][y]);
		}

		public void toNeighbors() {
			if (y-1 >= 0)
				to(x,y-1);
			if (x-1 >= 0)
				to(x-1,y);
			if (y+1 < Grid.this.rows)
				to (x,y+1);
			if (x+1 < Grid.this.cols)
				to (x+1, y);
		}
	}

}
