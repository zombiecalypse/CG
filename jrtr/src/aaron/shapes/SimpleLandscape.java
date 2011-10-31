package aaron.shapes;

import java.util.Random;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import aaron.colorizations.Color;
import aaron.colorizations.Colorization;
import aaron.colorizations.LimitColorization;
import jrtr.Shape;
import jrtr.VertexData;
import static aaron.Helpers.*;
import static java.lang.Math.*;

public class SimpleLandscape extends Shape {
	private static final Colorization color = new LimitColorization(
			Color.GREEN, Color.WHITE, Z, 0.5f);
	private static final float fluxMod = 0.125f;

	public SimpleLandscape() {
		super(makeLandscape(6, color));
	}

	/**
	 * Creates a random landscape mesh.
	 * 
	 * @param n
	 *            use 2^n +1 points
	 * @return the vertexdata with colour and positions.
	 */
	private static VertexData makeLandscape(int n, Colorization color) {
		assert n > 0;
		int dim = (int) (pow(2, n) + 1);
		float heightMap[][] = new float[dim][dim];
		randomHeightMap(heightMap, 0, 0, dim - 1, dim - 1);
		Grid grid = new Grid(dim + 2, dim + 2);
//		grid.setNormal(new Normalizator() {
//			@Override
//			public Vector3f normal(int row, int col, Tuple3f pos) {
//				return Z;
//			}
//		});
		float planeStep = 1.0f / ((float) dim);
		float min = Float.MAX_VALUE;
		for (int row = 0; row < dim; row++)
			for (int col = 0; col < dim; col++) {
				Vector3f v = new Vector3f(planeStep * row - 0.5f, planeStep
						* col - 0.5f, heightMap[row][col]);
				if (min > heightMap[row][col])
					min = heightMap[row][col];
				grid.set(row + 1, col + 1).to(v)
						.in(color.color(row + 1, col + 1, v));
			}
		min -= 0.2;
		for (int i = 0; i <= dim + 1; i++) {
			Vector3f left = new Vector3f(planeStep * i - 0.5f, -0.5f, min);
			Vector3f right = new Vector3f(planeStep * i - 0.5f, 0.5f, min);
			grid.set(i, 0).to(left).in(color.color(i, 0, left));
			grid.set(i, dim + 1).to(right).in(color.color(i, dim + 1, right));

			Vector3f bottom = new Vector3f(-0.5f, planeStep * i - 0.5f, min);
			Vector3f high = new Vector3f(0.5f, planeStep * i - 0.5f, min);
			grid.set(0, i).to(bottom).in(color.color(0, i, bottom));
			grid.set(dim + 1, i).to(high).in(color.color(dim + 1, i, high));
		}
		grid.connectNeighbors();
		return grid.vertexData();
	}

	private static void randomHeightMap(float[][] heightMap, int minX,
			int minY, int maxX, int maxY) {
		Random rand = new Random();
		heightMap[minX][minY] = rand.nextFloat();
		heightMap[maxX][minY] = rand.nextFloat();
		heightMap[minX][maxY] = rand.nextFloat();
		heightMap[maxX][maxY] = rand.nextFloat();
		randomHeightMapRecur(heightMap, rand, fluxMod, minX, minY, maxX, maxY);
	}

	private static void randomHeightMapRecur(float[][] heightMap, Random rand,
			float maxFlux, int minX, int minY, int maxX, int maxY) {
		if (maxX - minX < 2 && maxY - minY < 2)
			return;
		int centralX = (minX + maxX) / 2;
		int centralY = (minY + maxY) / 2;
		heightMap[centralX][centralY] = (float) (rand.nextGaussian() * maxFlux + (heightMap[minX][minY]
				+ heightMap[maxX][maxY] + heightMap[minX][maxY] + heightMap[maxX][minY]) / 4);

		if (heightMap[centralX][minY] == 0)
			heightMap[centralX][minY] = (float) (rand.nextGaussian() * maxFlux + (heightMap[minX][minY] + heightMap[maxX][minY]) / 2);
		if (heightMap[centralX][maxY] == 0)
			heightMap[centralX][maxY] = (float) (rand.nextGaussian() * maxFlux + (heightMap[minX][maxY] + heightMap[maxX][maxY]) / 2);
		if (heightMap[minX][centralY] == 0)
			heightMap[minX][centralY] = (float) (rand.nextGaussian() * maxFlux + (heightMap[minX][minY] + heightMap[minX][maxY]) / 2);
		if (heightMap[maxX][centralY] == 0)
			heightMap[maxX][centralY] = (float) (rand.nextGaussian() * maxFlux + (heightMap[maxX][minY] + heightMap[maxX][maxY]) / 2);

		randomHeightMapRecur(heightMap, rand, maxFlux * fluxMod, minX, minY,
				centralX, centralY);
		randomHeightMapRecur(heightMap, rand, maxFlux * fluxMod, minX,
				centralY, centralX, maxY);
		randomHeightMapRecur(heightMap, rand, maxFlux * fluxMod, centralX,
				minY, maxX, centralY);
		randomHeightMapRecur(heightMap, rand, maxFlux * fluxMod, centralX,
				centralY, maxX, maxY);
	}
}
