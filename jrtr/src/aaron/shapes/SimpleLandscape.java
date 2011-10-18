package aaron.shapes;

import java.util.Random;

import javax.vecmath.Vector3f;

import aaron.colorizations.Color;
import aaron.colorizations.Colorization;
import aaron.colorizations.LimitColorization;
import jrtr.Shape;
import jrtr.VertexData;
import static aaron.Helpers.*;
import static java.lang.Math.*;

public class SimpleLandscape extends Shape {
	private static final Colorization color = new LimitColorization(Color.GREEN, Color.WHITE, Z);
	public SimpleLandscape() {
		super(makeLandscape(10, color));
	}

	/**
	 * Creates a random landscape mesh.  
	 * @param n use 2^n +1 points
	 * @return the vertexdata with colour and positions.
	 */
	private static VertexData makeLandscape(int n, Colorization color) {
		assert n > 0;
		int dim = (int) (pow(2,n)+1); 
		float heightMap[][] = new float[dim][dim];
		randomHeightMap(heightMap,0,0,dim-1,dim-1);
		Grid grid = new Grid(dim,dim);
		float planeStep = 1.0f/((float)dim);
		for (int row = 0; row < dim ; row++)
			for (int col = 0; col < dim; col++) {
				Vector3f v = new Vector3f(planeStep*row, planeStep* col, heightMap[row][col]);
				grid.set(row, col)
						.to(v)
						.in(color.color(row, col, v));
			}
		return grid.vertexData();
	}

	private static void randomHeightMap(float[][] heightMap, 
			int minX, int minY,
			int maxX, int maxY) {
		Random rand = new Random();
		heightMap[minX][minY] = rand.nextFloat();
		heightMap[maxX][minY] = rand.nextFloat();
		heightMap[minX][maxY] = rand.nextFloat();
		heightMap[maxX][maxY] = rand.nextFloat();
		randomHeightMapRecur(heightMap, rand, 1, minX, minY, maxX, maxY);
	}

	private static void randomHeightMapRecur(float[][] heightMap, Random rand, float maxFlux,
			int minX, int minY, 
			int maxX, int maxY) {
		int centralX = (minX+maxX)/2;
		int centralY = (minY+maxY)/2;
		heightMap[centralX][centralY] =rand.nextFloat()*maxFlux+ (heightMap[minX][minY]+heightMap[maxX][maxY] + heightMap[minX][maxY] + heightMap[maxX][minY])/4;
		
		heightMap[centralX][minY] = rand.nextFloat()*maxFlux+ (heightMap[minX][minY]+ heightMap[maxX][minY])/2;
		heightMap[centralX][maxY] = rand.nextFloat()*maxFlux+ (heightMap[minX][maxY]+ heightMap[maxX][maxY])/2;
		heightMap[minX][centralY] = rand.nextFloat()*maxFlux+ (heightMap[minX][minY]+ heightMap[minX][maxX])/2;
		heightMap[maxX][centralY] = rand.nextFloat()*maxFlux+ (heightMap[maxX][minY]+ heightMap[maxX][maxX])/2;
		
		randomHeightMapRecur(heightMap, rand, maxFlux*0.5f, minX, minY, centralX, centralY);
		randomHeightMapRecur(heightMap, rand, maxFlux*0.5f, minX, centralY, centralX, maxY);
		randomHeightMapRecur(heightMap, rand, maxFlux*0.5f, centralX, minY, maxX, centralY);
		randomHeightMapRecur(heightMap, rand, maxFlux*0.5f, centralX, centralY, maxX, maxY);
	}
}
