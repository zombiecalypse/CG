package aaron.shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import aaron.shapes.Grid.Point;

import jrtr.VertexData;

/**
 * A Builder for VertexData that consists of a 2d grid.
 * 
 * @author Aaron
 * 
 */
public class Grid {
	private Point[][] points;
	private List<Point> additionalPoints;
	private Vector3f[][] normals;
	private Map<Point, List<Point>> connections;
	private int cols;
	private int rows;
	private boolean xconnected;
	private boolean yconnected;
	private Normalizator normal;
	
	class StdNormalizator implements Normalizator {

		@Override
		public Vector3f normal(int row, int col, Tuple3f pos) {
			int left = (col-1)%cols;
			left = left < 0 ? cols-1 : left;
			int right = (col+1)%cols;
			int over = (row + 1)%rows;
			int under = (row-1)%rows;
			under = under < 0 ? rows-1 : under;
			Vector3f gauss = new Vector3f();
			Vector3f dx = new Vector3f();
			dx.scaleAdd(3, points[left][row].position); 
			dx.scaleAdd(-3, points[right][row].position);
			dx.scaleAdd(1, points[right][under].position);
			dx.scaleAdd(-1, points[left][under].position);
			dx.scaleAdd(1, points[right][over].position);
			dx.scaleAdd(-1, points[left][over].position);

			Vector3f dy = new Vector3f();
			dy.scaleAdd(3, points[col][under].position);
			dy.scaleAdd(-3, points[col][over].position);
			dy.scaleAdd(1, points[left][over].position);
			dy.scaleAdd(-1, points[left][under].position);
			dy.scaleAdd(1, points[right][over].position);
			dy.scaleAdd(-1, points[right][under].position);

			gauss.cross(dx, dy);
			gauss.normalize();
			return gauss;
		}
		
	}
	
	public Grid setNormal(Normalizator n) {
		this.normal = n;
		return this;
	}

	public Grid(int cols, int rows) {
		this.rows = rows;
		this.cols = cols;
		this.points = new Point[cols][rows];
		this.normals = new Vector3f[cols][rows];
		this.connections = new HashMap<Point, List<Point>>();
		this.additionalPoints = new ArrayList<Point>();
		this.normal = new StdNormalizator();
		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++) {
				points[col][row] = new Point();
				normals[col][row] = new Vector3f();
			}
	}

	public Grid glueX() {
		for (int y = 0; y < this.rows; y++) {
			this.connect(0, y).to(this.cols - 1, y);
			this.connect(0, y).unsafeTo(this.cols - 1, y - 1);
		}
		this.xconnected = true;
		return this;
	}

	public Grid glueY() {
		for (int x = 0; x < this.cols; x++) {
			this.connect(x, 0).to(x, this.rows - 1);
			this.connect(x, 0).unsafeTo(x - 1, this.rows - 1);
		}
		this.yconnected = true;
		return this;
	}

	public VertexData vertexData() {
		VertexData data = new VertexData(cols * rows);
		float positionsArray[] = new float[cols * rows * 3];
		float colorArray[] = new float[cols * rows * 3];
		float normalArray[] = new float[cols * rows * 3];
		Map<Point, Integer> positionInArray = new HashMap<Point, Integer>();
		Map<Point, Vector3f> normalMap = getNormalMap();
		int flowIndex = 0;
		int index = 0;
		for (Point[] row : points)
			for (Point point : row) {
				Vector3f nvector = normalMap.get(point);
				if (nvector == null)
					nvector = new Vector3f();
				positionInArray.put(point, index++);
				positionsArray[flowIndex] = point.position.x;
				colorArray[flowIndex] = point.color.x;
				normalArray[flowIndex] = nvector.x;
				flowIndex++;
				positionsArray[flowIndex] = point.position.y;
				colorArray[flowIndex] = point.color.y;
				normalArray[flowIndex] = nvector.y;
				flowIndex++;
				positionsArray[flowIndex] = point.position.z;
				colorArray[flowIndex] = point.color.z;
				normalArray[flowIndex] = nvector.z;
				flowIndex++;
			}
		data.addElement(positionsArray, VertexData.Semantic.POSITION, 3);
		data.addElement(colorArray, VertexData.Semantic.COLOR, 3);
		data.addElement(normalArray, VertexData.Semantic.NORMAL, 3);
		data.addIndices(connectionArray(positionInArray));
		return data;
	}

	private Map<Point, Vector3f> getNormalMap() {
		Map<Point, Vector3f> normalMap = new HashMap<Point, Vector3f>();
		int botrow = 1, botcol = 1, toprow = rows-1, topcol = cols-1;
		if (xconnected) {
			botcol = 0;
			topcol = cols;
		}
		if (yconnected) {
			botrow = 0;
			toprow = rows;
		}
		for (int row = botrow; row < toprow; row++)
			for (int col = botcol; col < topcol; col++) {
				
				normalMap.put(points[col][row], normal.normal(row, col, points[col][row].position));
			}

		return normalMap;
	}

	private int[] connectionArray(Map<Point, Integer> positionInArray) {
		ArrayList<Integer> connectionsArray = new ArrayList<Integer>();
		for (Point from : this.connections.keySet()) {
			Point last = null;
			for (Point intermediate : this.connections.get(from)) {
				if (isConnected(last, intermediate)) {
					connectionsArray.add(positionInArray.get(from));
					connectionsArray.add(positionInArray.get(last));
					connectionsArray.add(positionInArray.get(intermediate));
				}
				last = intermediate;
			}
		}
		int bare[] = new int[connectionsArray.size()];
		for (int i = 0; i < connectionsArray.size(); i++) { // Autoboxing is SO
															// not auto enough
			bare[i] = connectionsArray.get(i);
		}
		return bare;
	}

	private boolean isConnected(Point one, Point two) {
		if (one == null || two == null)
			return false;
		else {
			List<Point> neighborsOfOne = this.connections.get(one);
			List<Point> neighborsOfTwo = this.connections.get(two);
			return neighborsOfOne.contains(two) || neighborsOfTwo.contains(one);
		}
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
			if (Grid.this.points[x][y] == null)
				Grid.this.points[x][y] = new Point();
		}

		public Setter to(Vector3f torusPiece) {
			Grid.this.points[x][y].position = torusPiece;
			return this;
		}

		public Setter in(Vector3f v) {
			Grid.this.points[x][y].color = v;
			return this;
		}

		public Setter to(float f, float g, float h) {
			return to(new Vector3f(f, g, h));

		}
	}

	class Point {
		public Vector3f position;
		public Vector3f color;

		public Point() {
			position = new Vector3f();
			color = new Vector3f();
		}
	}

	class Connector {
		private Point here;
		private int x;
		private int y;

		public Connector(Grid grid, int x, int y) {
			assert x < Grid.this.points.length;
			assert y < Grid.this.points[0].length;
			this.here = Grid.this.points[x][y];
			this.x = x;
			this.y = y;
		}

		public void to(int x, int y) {
			assert x < Grid.this.points.length && x >= 0;
			assert y < Grid.this.points[0].length && y >= 0;
			unsafeTo(x, y);
		}

		public void unsafeTo(int x, int y) {
			if (!(x < Grid.this.points.length && x >= 0))
				return;
			if (!(y < Grid.this.points[0].length && y >= 0))
				return;

			Point there = Grid.this.points[x][y];

			if (!Grid.this.connections.containsKey(this.here))
				Grid.this.connections.put(this.here, new ArrayList<Point>());

			if (!Grid.this.connections.containsKey(there))
				Grid.this.connections.put(there, new ArrayList<Point>());

			Grid.this.connections.get(this.here).add(there);
			Grid.this.connections.get(there).add(this.here);
		}

		public void toNeighbors() {
			unsafeTo(x, y - 1);
			unsafeTo(x, y + 1);

			unsafeTo(x - 1, y);
			unsafeTo(x + 1, y);

			unsafeTo(x - 1, y - 1);
			unsafeTo(x - 1, y + 1);

			unsafeTo(x + 1, y - 1);
			unsafeTo(x + 1, y + 1);
		}
	}

	class Triag {
		private int x1;
		private int y1;
		private int x2;
		private int y2;
		private int x3;
		private int y3;

		public Triag one(int x, int y) {
			this.x1 = x;
			this.y1 = y;
			return this;
		}

		public Triag two(int x, int y) {
			this.x2 = x;
			this.y2 = y;
			return this;
		}

		public Triag three(int x, int y) {
			this.x3 = x;
			this.y3 = y;
			return this;
		}

		private int index(int x, int y) {
			return Grid.this.rows * x + y;
		}

		public int[] toArray() {
			int indeces[] = new int[3];
			indeces[0] = index(x1, y1);
			indeces[1] = index(x2, y2);
			indeces[2] = index(x3, y3);
			return indeces;
		}
	}

	public Grid connectNeighbors() {
		for (int x = 0; x < this.cols; x++) {
			for (int y = 0; y < this.rows; y++) {
				connect(x, y).toNeighbors();
			}
		}
		return this;
	}

}
