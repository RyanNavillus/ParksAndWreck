#version 330

uniform sampler2D renderedTexture;

out vec4 gl_FragColor;

float noise(in vec2 coordinate)
{
    return fract(sin(dot(coordinate, vec2(12.9898, 78.233)))*43758.5453);
}

void main() {
    // Pass through our original color with full opacity.
    float luma = noise(floor(gl_FragCoord.xy / 6f));
    //float multiplier = 1;
    float multiplier = .7f + (gl_FragCoord.x / 1920.0f) * .15f + (gl_FragCoord.x / 1080.0f) * .1f;
    luma *= .1f;
    gl_FragColor = vec4((1 - luma) * red * multiplier, (1 - luma) * green * multiplier, (1 - luma) * blue * multiplier, 1);
    gl_FragDepth = -10;
}