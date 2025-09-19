package me.udnek.fnafu.misc

import com.destroystokyo.paper.ParticleBuilder
import com.google.common.base.Predicate
import com.google.common.base.Strings
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


object Utils{
    fun freddySpawnDustParticle(location: Location) = spawnDustParticle(location.add(0.0, 1.0, 0.0), Color.fromRGB(139, 0, 255), 2.0, 1.0)
    fun spawnDustParticle(location: Location, color: Color, height: Double, width: Double) =
        Particle.DUST.builder().location(location).data(DustOptions(color, 2.4f)).count(50 * (2.0.pow(height * width)).toInt())
            .offset(width / 2, height / 2, width / 2).spawn()


    fun spawnDustCircle(particleBuilder: ParticleBuilder, radius: Double, angleDegrees: Double) =
        spawnDustCircle(particleBuilder, radius, angleDegrees, { return@spawnDustCircle true })
    fun spawnDustCircle(particleBuilder: ParticleBuilder, radius: Double, angleDegrees: Double, consumer: Predicate<Location>){
        val location = particleBuilder.location() ?: throw IllegalArgumentException(Strings.lenientFormat( "Location must be not null"))
        var d = 0.0
        while (d <= 360) {
            val particleLoc = Location(location.world, location.x, location.y, location.z)
            particleLoc.x = location.x + cos(Math.toRadians(d)) * radius
            particleLoc.z = location.z + sin(Math.toRadians(d)) * radius
            if (consumer.apply(particleLoc)) particleBuilder.location(particleLoc).spawn()
            d += angleDegrees
        }
    }
}