#ifdef GL_ES
precision mediump float;
#endif

// Input from vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;

// Spotlight parameters
uniform vec2 resolution;      // Screen resolution (width, height)
uniform vec2 playerPosition;  // Player position in screen coordinates
uniform float radius;         // Radius of the spotlight
uniform float softness;       // Softness of the spotlight edge (0.0 to 1.0)
uniform float darkness;       // How dark the shadowed area should be (0.0 to 1.0)

// The scene texture (everything that was rendered to the framebuffer)
uniform sampler2D u_texture;

void main() {
    // Get current pixel position in screen coordinates
    vec2 fragCoord = v_texCoord * resolution;

    // Calculate distance from current pixel to player position
    float distance = length(fragCoord - playerPosition);

    // Calculate spotlight mask (1.0 inside spotlight, 0.0 outside)
    // The smoothstep creates a smooth transition at the edge
    float spotMask = 1.0 - smoothstep(radius - (radius * softness), radius, distance);

    // Sample the original scene color
    vec4 originalColor = texture2D(u_texture, v_texCoord) * v_color;

    // Darken the color outside the spotlight
    // Mix between original color and darkened color based on the spotlight mask
    vec4 darkColor = originalColor * (1.0 - darkness);
    vec4 finalColor = mix(darkColor, originalColor, spotMask);

    gl_FragColor = finalColor;
}
