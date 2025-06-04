// blackwhite.frag
#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;

// Luminance coefficients for converting RGB to grayscale
// These values are based on human eye sensitivity to different colors
const vec3 luminance = vec3(0.299, 0.587, 0.114);

void main() {
    // Sample the original color from the texture
    vec4 color = texture2D(u_texture, v_texCoords);

    // Calculate grayscale value using luminance formula
    float gray = dot(color.rgb, luminance);

    // Output the grayscale color while preserving alpha
    gl_FragColor = vec4(gray, gray, gray, color.a);
}
