#version 150

uniform sampler2D texture;

// Input variable, passed from vertex to fragment shader
// and interpolated automatically to each fragment
in vec4 normal;
in vec2 textureCoord;

// Output variable, will be written to framebuffer automatically
out vec4 out_color;

vec4 light() {
	vec4 ret;
	float glossiness = dot(texture2D(glossmap, textureCoord), texture2D(glossmap, textureCoord));
	for (int i = 0; i < 8; i++) {
		vec4 lightdir = normalize(gl_LightSource[i].position - gl_Position);
		vec4 eyelight = reflect(normalize(lightdir),normal);
		float ldirank = max(0.0,dot(lightdir, normal));
		float leyeank = max(0.0,dot(eyelight, eye));
		ret += ldirank *gl_LightSource[i].diffuse;
		ret += gl_FrontMaterial.specular * 
								pow(leyeank, glossiness) * 
								gl_LightSource[i].specular;
	}
  return ret;
}
void main() {		
	out_color = texture2D(texture, textureCoord)+ light();
}
