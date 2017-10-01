uniform vec2 window;
uniform sampler2D renderedTexture;
uniform float time;

vec2 CRTCurveUV( vec2 uv )
{
    uv = uv * 2.0 - 1.0;
    vec2 offset = abs( uv.yx ) / vec2( 6.0, 4.0 );
    uv = uv + uv * offset * offset;
    uv = uv * 0.5 + 0.5;
    return uv;
}

void DrawVignette( inout vec3 color, vec2 uv )
{
    float vignette = uv.x * uv.y * ( 1.0 - uv.x ) * ( 1.0 - uv.y );
    vignette = clamp( pow( 16.0 * vignette, 0.3 ), 0.0, 1.0 );
    color *= vignette;
}

void DrawScanline( inout vec3 color, vec2 uv )
{
    float scanline 	= clamp( 0.95 + 0.05 * cos( 3.14 * ( uv.y + 0.008 ) * 240.0 * 1.0 ), 0.0, 1.0 );
    float grille 	= 0.8 + 0.2 * clamp( 1.5 * cos( 3.14 * uv.x * 640.0 * 1.0 ), 0.0, 1.0 );
    color *= scanline * grille * 1.4;
}

float noise(in vec2 coordinate)
{
    return fract(sin(dot(coordinate, vec2(12.9898 * time, 78.233)))*43758.5453);
}


void main() {
    vec2 uv    = gl_FragCoord.xy / window.xy;
    vec3 color = texture(renderedTexture, uv).xyz;
    vec2 crtUV = CRTCurveUV( uv );
    if ( crtUV.x < 0.0 || crtUV.x > 1.0 || crtUV.y < 0.0 || crtUV.y > 1.0 )
    {
        color = vec3( 0.0, 0.0, 0.0 );
    }

    DrawScanline( color, uv );
    float luma = noise(floor(gl_FragCoord.xy / 3));
    luma *= .1;
    color *= (1 - luma);
    DrawVignette( color, crtUV );

	gl_FragColor.xyz 	= color;
    gl_FragColor.w		= 1.0;
}