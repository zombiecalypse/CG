#version 150
uniform vec4 eye;
uniform sampler2D texture;
uniform sampler2D glossmap;

const inout vec4 normal;
const in vec4 position;
const inout vec2 textureCoord;

void main() {
	gl_Position = position;
}
