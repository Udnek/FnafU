#version 150

// Can't moj_import in things used during startup, when resource packs don't exist.
// This is a copy of dynamicimports.glsl
layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
    float LineWidth;
};

in vec4 vertexColor;

out vec4 fragColor;

void main() {

    vec4 color = vertexColor;

    if (color.a == 0.0) {discard;}

	/* Inventory background */
    else if (color.r == 16/255.0 && color.g == 16/255.0 && color.b == 16/255.0) {
        color = vec4(0.0, 0.0, 0.0, 0.0);
    }

    fragColor = color * ColorModulator;
}
