#version 150
uniform vec4 eye;

in vec4 normal;
in vec4 color;
in vec4 position;

out vec4 frag_normal;
out vec4 frag_color;

vec4 lighting() {
	vec4 ret;
	for (int i = 0; i < 8; i++) {
		vec4 lightdir = normalize(gl_LightSource[i].position - position);
		vec4 eyelight = reflect(normalize(lightdir),normal);
		float ldirank = max(0.0,dot(lightdir, normal));
		float leyeank = max(0.0,dot(eyelight, eye));
		ret += ldirank *gl_LightSource[i].diffuse;
		ret += gl_FrontMaterial.specular * pow(leyeank, gl_FrontMaterial.shininess) * gl_LightSource[i].specular;
	}
}

void main() {
	gl_Position = position;
	frag_normal = normal;
  frag_color = lighting();
}
