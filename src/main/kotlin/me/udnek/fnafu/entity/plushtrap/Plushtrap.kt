package me.udnek.fnafu.entity.plushtrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntity
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Drowned
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector


class Plushtrap : ConstructableCustomEntity<Drowned>() {

    companion object {
        const val RUNNING_TIME: Int = 20 * 5
        const val RUNNING_MULTIPLIER: Float = 0.2f
        const val SEEK_TARGET_RADIUS: Double = 20.0
        const val NO_TARGET_DESPAWN_TIME: Int = 20 * 10
        const val KILL_TARGET_RADIUS: Double = 0.5
    }

    var step: Int = 0
    var noTargetTime: Int = 0
    lateinit var game: FnafUGame
    lateinit var owner: FnafUPlayer
    var target: FnafUPlayer? = null

    override fun delayedTick() {
        damageNearestPlayers()
        if (step < RUNNING_TIME){
            entity.velocity = entity.location.direction.setY(0).multiply(RUNNING_MULTIPLIER)
        } else if (step == RUNNING_TIME) {
            entity.velocity = Vector()
        } else {
            if (target != null && getDistanceToVisibleSurvivor(target!!) != null){
                target(target!!)
            } else {
                val player = findNearestVisibleSurvivor()
                if (player == null) noTarget()
                else target(player)
            }
            print(target)
            if (target != null) {
                val distanceToVisibleSurvivor = getDistanceToVisibleSurvivor(target!!)
                print(distanceToVisibleSurvivor)
            }
        }

        if (noTargetTime >= NO_TARGET_DESPAWN_TIME) remove()

        step += tickDelay
    }

    private fun noTarget() {
        Nms.get().stop(entity)
        noTargetTime += tickDelay
        entity.isAware = false
        target = null
    }

    private fun target(player: FnafUPlayer) {
        noTargetTime = 0
        Nms.get().follow(entity, player.player)
        if (target != player) {
            entity.isAware = true
            target = player
        }
    }

    override fun remove() {
        owner.data.get(FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA)?.let {
            it.plushtrap = null
            it.abilityItem!!.components.getOrDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.getOrException(FnafUComponents.SPRINGTRAP_PLUSHTRAP_ABILITY)
                .cooldown(it.abilityItem!!, owner.player)
        }
        super.remove()
    }

    private fun damageNearestPlayers(){
        game.findNearbyPlayers(entity.location, KILL_TARGET_RADIUS).forEach { it.damage() }
    }

    private fun findNearestVisibleSurvivor() : FnafUPlayer? {
        val visiblePlayers = HashMap<Double, FnafUPlayer>()
        for (player in game.findNearbyPlayers(entity.location, SEEK_TARGET_RADIUS)) {
            if (player.type != FnafUPlayer.Type.SURVIVOR) continue
            visiblePlayers.put(getDistanceToVisibleSurvivor(player)?: continue, player)
        }
        return visiblePlayers.minByOrNull { it.key }?.value
    }

    fun getDistanceToVisibleSurvivor(player: FnafUPlayer): Double? {
        val targetEyeLocation = player.player.eyeLocation
        val entityEyeLocation = entity.eyeLocation
        val distance = entityEyeLocation.distance(targetEyeLocation)
        debag(entityEyeLocation, targetEyeLocation.toVector().subtract(entityEyeLocation.toVector()).normalize())
        val rayTraceResult = entity.world.rayTrace(entityEyeLocation, targetEyeLocation.toVector().subtract(entityEyeLocation.toVector()).normalize(),
            distance, FluidCollisionMode.NEVER, true, 0.0, null) ?: return null

        if (rayTraceResult.hitBlock == null) return distance
        return null
    }

    fun debag(startLocation: Location, direction: Vector) {
        val particle = Particle.SMALL_GUST
        for (i in 1..7) {
            val k = i / 2
            val point = startLocation.clone().add(direction.clone().multiply(k));
            startLocation.world.spawnParticle(particle, point, 1)
        }
    }

    fun onEntityHit(event: EntityDamageByEntityEvent) {
        (event.entity as? Player)?.getFnafU()?.also { it.damage() }
        event.isCancelled = true
    }

    override fun getType(): CustomTickingEntityType<*> {
        return EntityTypes.PLUSHTRAP
    }

    override fun getTickDelay() = 2
}