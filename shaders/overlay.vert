#version 330

in vec3 in_Position;
in vec4 in_Color;
in vec3 in_Normal;
in vec2 in_TexCoord;

out vec2 texCoord;

void main() {
    texCoord = in_TexCoord;
	vec3 pos = vec3(in_Position.x / 960 - 1, 1 - in_Position.y / 540, -1);
    gl_Position = vec4(pos, 1.0);
}