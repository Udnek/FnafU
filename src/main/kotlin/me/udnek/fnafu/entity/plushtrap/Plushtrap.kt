package me.udnek.fnafu.entity.plushtrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntity
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import org.bukkit.FluidCollisionMode
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
        const val KILL_TARGET_RADIUS: Double = 0.6
    }

    var step: Int = 0
    var noTargetTime: Int = 0
    lateinit var game: FnafUGame
    lateinit var owner: FnafUPlayer
    lateinit var initialDirection: Vector
    var target: FnafUPlayer? = null



    override fun delayedTick() {
        damageNearestPlayers()
        if (step < RUNNING_TIME){
            Nms.get().moveNaturally(entity, initialDirection.clone().setY(0).multiply(RUNNING_MULTIPLIER))
        } else if (step == RUNNING_TIME) {
            entity.velocity = Vector()
            entity.setRotation(entity.yaw + 180, entity.pitch)
        } else {
            if (target != null && getDistanceToVisibleSurvivor(target!!) != null){
                target(target!!)
            } else {
                val player = findNearestVisibleSurvivor()
                if (player == null) noTarget()
                else target(player)
            }
        }

        if (noTargetTime >= NO_TARGET_DESPAWN_TIME) {
            Sounds.PLUSHTRAP_RUN.play(entity.location)
            remove()
        }

        step += tickDelay
    }

    private fun noTarget() {
        Nms.get().stopMovingWithPathfind(entity)
        entity.velocity = Vector()
        noTargetTime += tickDelay
        entity.isAware = false
        target = null
    }

    private fun target(player: FnafUPlayer) {
        if (step % 20 == 0) Sounds.PLUSHTRAP_NEAR.play(player.player)
        noTargetTime = 0
        Nms.get().moveWithPathfind(entity, player.player.location)
        if (target != player) {
            entity.isAware = true
            target = player
        }
    }

    override fun remove() {
        PlushtrapType.spawnParticle(entity)
        owner.data.get(FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA)?.let {
            it.plushtrap = null
            it.abilityItem!!.components.getOrDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.getOrException(FnafUComponents.SPRINGTRAP_PLUSHTRAP_ABILITY)
                .cooldown(it.abilityItem!!, owner.player)
        }
        super.remove()
    }

    private fun damageNearestPlayers(){
        game.findNearbyPlayers(entity.location, KILL_TARGET_RADIUS).forEach { it.damage(Sounds.SPRINGTRAP_JUMP_SCARE) }
    }

    private fun findNearestVisibleSurvivor() : FnafUPlayer? {
        val visiblePlayers = HashMap<Double, FnafUPlayer>()
        for (player in game.findNearbyPlayers(entity.location, SEEK_TARGET_RADIUS)) {
            if (player.type != FnafUPlayer.Type.SURVIVOR) continue
            if (player.status != FnafUPlayer.Status.ALIVE) continue
            visiblePlayers.put(getDistanceToVisibleSurvivor(player)?: continue, player)
        }
        return visiblePlayers.minByOrNull { it.key }?.value
    }

    fun getDistanceToVisibleSurvivor(player: FnafUPlayer): Double? {
        if (player.status == FnafUPlayer.Status.DEAD) return null
        val targetEyeLocation = player.player.eyeLocation
        val entityEyeLocation = entity.eyeLocation
        val distance = entityEyeLocation.distance(targetEyeLocation)
        if (distance > SEEK_TARGET_RADIUS) return null

        val direction = targetEyeLocation.toVector().subtract(entityEyeLocation.toVector())
        entity.world.rayTraceBlocks(entityEyeLocation, direction, distance, FluidCollisionMode.NEVER, true)
            ?: return distance
        return null
    }

    fun onEntityHit(event: EntityDamageByEntityEvent) {
        (event.entity as? Player)?.getFnafU()?.also { it.damage(Sounds.PLUSHTRAP_RUN) }
        event.isCancelled = true
    }

    override fun getType(): CustomTickingEntityType<*> {
        return EntityTypes.PLUSHTRAP
    }

    override fun getTickDelay() = 1
}