#version 330

uniform float red;
uniform float green;
uniform float blue;

out vec4 gl_FragColor;

float noise(in vec2 coordinate)
{
    return fract(sin(dot(coordinate, vec2(12.9898, 78.233)))*43758.5453);
}

void main() {
    float luma = noise(floor(gl_FragCoord.xy / 3f));
    luma *= .1;
    gl_FragColor = vec4((1 - luma) * red, (1 - luma) * green, (1 - luma) * blue, 1);
    gl_FragDepth = -10;
}