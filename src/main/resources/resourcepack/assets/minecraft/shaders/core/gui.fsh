#version 150

in vec4 vertexColor;

uniform vec4 ColorModulator;

out vec4 fragColor;

void main() {

    vec4 color = vertexColor;

    if (color.a == 0.0){
        discard;
    }
    
	/* Inventory background */
    else if (color.r == 16/255.0 && color.g == 16/255.0 && color.b == 16/255.0) {
        color = vec4(0.0, 0.0, 0.0, 0.0);
    }

    fragColor = color * ColorModulator;
}
