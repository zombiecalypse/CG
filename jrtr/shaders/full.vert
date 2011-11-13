#version 150

in vec4 normal;
in vec4 color;
in vec4 position;

out vec4 frag_normal;
out vec4 frag_color;

void main() {
	for (int i = 0; i < GL_MAX_LIGHTS; i++) {
		vec4 lightdir = reflect(gl_LightSource[i].position - position, normal);
		float ank = dot(lightdir, position);
	}
	gl_Position = position;
}
