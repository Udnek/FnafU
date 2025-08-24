#version 150

layout(std140) uniform LightmapInfo {
    float AmbientLightFactor;
    float SkyFactor;
    float BlockFactor;
    int UseBrightLightmap;
    float NightVisionFactor;
    float DarknessScale;
    float DarkenWorldFactor;
    float BrightnessFactor;
    vec3 SkyLightColor;
} lightmapInfo;

in vec2 texCoord;

out vec4 fragColor;

float get_brightness(float level) {
    float curved_level = level / (4.0 - 3.0 * level);
    return mix(curved_level, 1.0, lightmapInfo.AmbientLightFactor);
}

vec3 notGamma(vec3 x) {
    vec3 nx = 1.0 - x;
    return 1.0 - nx * nx * nx * nx;
}

bool isDeadDarkness(){
    return lightmapInfo.NightVisionFactor > 0;
}

void main() {
    float block_brightness = get_brightness(floor(texCoord.x * 16) / 15) * lightmapInfo.BlockFactor;
    float sky_brightness = get_brightness(floor(texCoord.y * 16) / 15) * lightmapInfo.SkyFactor;

    //cubic nonsense, dips to yellowish in the middle, white when fully saturated
    vec3 darkness = vec3(11, 11, 13);
    darkness/=255.0;

    vec3 color = vec3(0);

    // color += vec3(
    //     block_brightness,
    //     block_brightness * 0.6,
    //     block_brightness * 0.05
    // );
    float mul;
    float add;
    if (isDeadDarkness()){
        mul = 0.9;
        add = 1 - mul;
    } else {
        mul = 0.65;
        add = 1 - mul;
    }
    color += vec3(
        block_brightness,
        block_brightness * ((block_brightness * mul + add) * mul + add),
        block_brightness * (block_brightness * block_brightness * mul + add)
    );

    if (isDeadDarkness()){
        if (block_brightness > 0){
            color += darkness;
        }
    } else {
        color += darkness;
    }

    color += sky_brightness;

    if (lightmapInfo.BrightnessFactor > 1){
        color = vec3(1);
    }

    fragColor = vec4(color, 1.0);

    // vec3 color = vec3(
    //     0, 0, 0
    // );

    // if (lightmapInfo.UseBrightLightmap != 0) {
    //     color = mix(color, vec3(0.99, 1.12, 1.0), 0.25);
    //     color = clamp(color, 0.0, 1.0);
    // } else {
    //     color += lightmapInfo.SkyLightColor * sky_brightness;
    //     color = mix(color, vec3(0.75), 0.04);

    //     vec3 darkened_color = color * vec3(0.7, 0.6, 0.6);
    //     color = mix(color, darkened_color, lightmapInfo.DarkenWorldFactor);
    // }

    // if (lightmapInfo.NightVisionFactor > 0.0) {
    //     // scale up uniformly until 1.0 is hit by one of the colors
    //     float max_component = max(color.r, max(color.g, color.b));
    //     if (max_component < 1.0) {
    //         vec3 bright_color = color / max_component;
    //         color = mix(color, bright_color, lightmapInfo.NightVisionFactor);
    //     }
    // }

    // if (lightmapInfo.UseBrightLightmap == 0) {
    //     color = clamp(color - vec3(lightmapInfo.DarknessScale), 0.0, 1.0);
    // }

    // vec3 notGamma = notGamma(color);
    // color = mix(color, notGamma, lightmapInfo.BrightnessFactor);
    // color = mix(color, vec3(0.75), 0.04);
    // color = clamp(color, 0.0, 1.0);
}
