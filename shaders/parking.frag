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
    float luma = noise(floor(gl_FragCoord.xy / 6f));
    if(luma < .25)
    {
        gl_FragColor = vec4(1, 174.0f / 255.0f, 0f, 0);
    }
    else
    {
        gl_FragColor = vec4(1, 174.0f / 255.0f, 0f, .5f);
    }
    gl_FragDepth = -10;
}