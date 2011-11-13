#version 150

in vec4 normal;
in vec4 color;
in vec4 position;

out vec4 frag_normal;
out vec4 frag_color;

vec4 diffuse() {
	vec4 ret;
	for (int i = 0; i < 8; i++) {
		vec4 lightdir = reflect(gl_LightSource[i].position - position, normal);
		float ank = max(0.0,dot(lightdir, normal));
		ret += ank *gl_LightSource[i].diffuse;
	}
}
void main() {
	gl_Position = position;
	frag_normal = normal;
  frag_color = diffuse();
}
