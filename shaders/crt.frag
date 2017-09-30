#version 330

uniform float red;
uniform float green;
uniform float blue;

out vec4 gl_FragColor;

void main() {
    gl_FragColor = vec4(red, green, blue, 1);
    gl_FragDepth = -10;
}