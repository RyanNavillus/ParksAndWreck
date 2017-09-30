#version 330

uniform sampler2D renderedTexture;

float noise(in vec2 coordinate)
{
    return fract(sin(dot(coordinate, vec2(12.9898, 78.233)))*43758.5453);
}

void main() {
    float luma = noise(floor(gl_FragCoord.xy / 6));
    float multiplier = .7 + (gl_FragCoord.x / 1920.0) * .15 + (gl_FragCoord.x / 1080.0) * .1;
    luma *= .1;
    vec4 color = texture(renderedTexture, vec2(gl_FragCoord.x / 1920.0, gl_FragCoord.y / 1080.0));
    gl_FragColor = vec4((1 - luma) * color.x * multiplier, (1 - luma) * color.y * multiplier, (1 - luma) * color.z * multiplier, 1);
    gl_FragDepth = -10;
}