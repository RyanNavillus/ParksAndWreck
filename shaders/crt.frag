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
    // Pass through our original color with full opacity.
    float luma = noise(gl_FragCoord.xy);
    float multiplier = 1;
    //float multiplier = .7f + (gl_FragCoord.x / 1920f) * .15f + (gl_FragCoord.x / 1080f) * .1f;
    luma *= .1f;
    gl_FragColor = vec4((1 - luma) * red * multiplier, (1 - luma) * green * multiplier, (1 - luma) * blue * multiplier, 1);
    gl_FragDepth = -10;
}