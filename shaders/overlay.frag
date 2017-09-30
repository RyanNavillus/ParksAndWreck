#version 330

uniform vec2 window;
uniform sampler2D renderedTexture;
uniform float time;

void main() {
    vec4 color = texture(renderedTexture, vec2(gl_FragCoord.x / 1920.0, gl_FragCoord.y / (1080.0)));
    gl_FragColor = vec4(color.x, color.y, color.z, 1);
    gl_FragDepth = -10;
}