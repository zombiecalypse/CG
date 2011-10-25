package aaron.shapes;

import jrtr.Shape;
import jrtr.VertexData;

public class House extends Shape {
	public House() {
		super(makeHouse());
	}

	public static VertexData makeHouse() {
		// A house
		float vertices[] = { -4, -4, 4, 4, -4, 4, 4, 4, 4, -4, 4,
				4, // front face
				-4, -4, -4, -4, -4, 4, -4, 4, 4, -4, 4,
				-4, // left face
				4, -4, -4, -4, -4, -4, -4, 4, -4, 4, 4,
				-4, // back face
				4, -4, 4, 4, -4, -4, 4, 4, -4, 4, 4,
				4, // right face
				4, 4, 4, 4, 4, -4, -4, 4, -4, -4, 4,
				4, // top face
				-4, -4, 4, -4, -4, -4, 4, -4, -4, 4, -4,
				4, // bottom face

				-20, -4, 20, 20, -4, 20, 20, -4, -20, -20, -4,
				-20, // ground floor
				-4, 4, 4, 4, 4, 4, 0, 8,
				4, // the roof
				4, 4, 4, 4, 4, -4, 0, 8, -4, 0, 8, 4, -4, 4, 4, 0, 8, 4, 0, 8,
				-4, -4, 4, -4, 4, 4, -4, -4, 4, -4, 0, 8, -4 };

		float normals[] = { 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
				0,
				1, // front face
				-1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
				0,
				0, // left face
				0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
				0,
				-1, // back face
				1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
				0,
				0, // right face
				0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				1,
				0, // top face
				0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
				-1,
				0, // bottom face

				0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				1,
				0, // ground floor
				0, 0, 1, 0, 0, 1, 0,
				0,
				1, // front roof
				0.707f, 0.707f, 0, 0.707f, 0.707f, 0, 0.707f, 0.707f, 0,
				0.707f, 0.707f,
				0, // right roof
				-0.707f, 0.707f, 0, -0.707f, 0.707f, 0, -0.707f, 0.707f, 0,
				-0.707f, 0.707f, 0, // left roof
				0, 0, -1, 0, 0, -1, 0, 0, -1 }; // back roof

		float colors[] = { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1,
				0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,

				0, 0.5f, 0, 0, 0.5f, 0, 0, 0.5f, 0, 0, 0.5f,
				0, // ground floor
				0, 0, 1, 0, 0, 1, 0, 0,
				1, // roof
				1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
				0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, };

		// Set up the vertex data
		VertexData vertexData = new VertexData(42);

		// Specify the elements of the vertex data:
		// - one element for vertex positions
		vertexData.addElement(vertices, VertexData.Semantic.POSITION, 3);
		// - one element for vertex colors
		vertexData.addElement(colors, VertexData.Semantic.COLOR, 3);
		// - one element for vertex normals
		vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);

		// The index data that stores the connectivity of the triangles
		int indices[] = { 0, 2, 3, 0, 1, 2, // front face
				4, 6, 7, 4, 5, 6, // left face
				8, 10, 11, 8, 9, 10, // back face
				12, 14, 15, 12, 13, 14, // right face
				16, 18, 19, 16, 17, 18, // top face
				20, 22, 23, 20, 21, 22, // bottom face

				24, 26, 27, 24, 25, 26, // ground floor
				28, 29, 30, // roof
				31, 33, 34, 31, 32, 33, 35, 37, 38, 35, 36, 37, 39, 40, 41 };

		vertexData.addIndices(indices);

		return vertexData;
	}

}
