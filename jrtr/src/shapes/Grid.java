package shapes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector4f;

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

	private Connector connect(int x, int y) {
		return new Connector(this, x, y);
	}
	
	class Connector {
		private Vector4f here;
		private Grid grid;

		public Connector(Grid grid, int x, int y) {
			this.grid = grid;
			this.here = grid.points[x][y];
		}
		
		public Connector to(int x, int y) {
			grid.
		}
	}
}
