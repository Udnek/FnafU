package me.udnek.fnafu.misc

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import kotlin.math.pow

object Utils{
    fun freddySpawnDustParticle(location: Location) = spawnDustParticle(location.add(0.0, 1.0, 0.0), Color.fromRGB(139, 0, 255), 2.0, 1.0)
    fun spawnDustParticle(location: Location, color: Color, height: Double, width: Double) {
        Particle.DUST.builder().location(location).data(Particle.DustOptions(color, 2.4f)).count(50 * (2.0.pow(height * width)).toInt())
            .offset(width / 2, height / 2, width / 2).spawn()
    }
}