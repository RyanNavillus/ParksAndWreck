uniform vec2 window;
uniform sampler2D renderedTexture;
uniform float time;

const int samples = 4;

vec2 aspect;
vec2 aux;
float angle;
float cellWidth;
mat2 rot;
mat2 unRot;

vec4 samplePixel(vec2 uv) {
    uv = uv * aspect * rot;

    // [0,1) range indicating fragment's position within current cell
    // Useful for drawing stuff inside cells
    vec2 cellCoord = fract(uv / cellWidth);

    // "Tile-space" coordinate of cell containing current fragment
    // Will be same value for every fragment within the cell
    vec2 cellId = floor(uv / cellWidth);

    // Texture coordinate (0-1 range across entire quad) of bottom-left corner of current cell
    vec2 mosaicUv = cellId * cellWidth * unRot / aspect;

	return texture(renderedTexture, mosaicUv);
}

vec4 superSamplePixel(vec2 pos) {
    vec2 off = 1.0 / float(samples) / window.xy;
    vec4 sum = vec4(0.0);
    for (int x=0; x<samples; x++) {
        for (int y=0; y<samples; y++) {
            sum += samplePixel(pos + vec2(off.x*float(x), off.y*float(y)));
        }
    }
    return sum / float(samples * samples);
}

void main() {
    aspect = vec2(window.x/window.y, 1.0);
        aux = vec2(100, 100) / window.xy;
        angle = 0;
        cellWidth = .001083;
        rot = mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
        unRot = inverse(rot);


        vec2 uv = gl_FragCoord.xy / window.xy;
        gl_FragColor = superSamplePixel(uv);
        //fragColor = samplePixel(uv); //Uncomment for no-AA version
}