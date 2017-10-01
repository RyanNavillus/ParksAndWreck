#version 330

uniform vec2 window;
uniform sampler2D renderedTexture;

out vec4 gl_FragColor;

void main() {
    vec2 uv = gl_FragCoord.xy / window;
    gl_FragColor = texture(renderedTexture, uv);
    gl_FragDepth = -10;
}