package me.udnek.fnafu.entity.plushtrap

import me.udnek.fnafu.component.animatronic.SpringtrapPlushtrapAbility
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customentitylike.entity.ConstructableCustomEntity
import me.udnek.itemscoreu.customentitylike.entity.CustomTickingEntityType
import org.bukkit.entity.Drowned
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector


class Plushtrap : ConstructableCustomEntity<Drowned>() {

    companion object {
        const val RUNNING_TIME: Int = 20 * 5
        const val RUNNING_MULTIPLIER: Float = 0.2f
        const val SEEK_TARGET_RADIUS: Float = 20f
        const val NO_TARGET_DESPAWN_TIME: Int = 20 * 10
        const val KILL_TARGET_RADIUS: Float = 0.5f
    }

    var step: Int = 0
    var noTargetTime: Int = 0
    lateinit var game: FnafUGame
    lateinit var ability: SpringtrapPlushtrapAbility

    override fun delayedTick() {
        if (step < RUNNING_TIME){
            damageNearestPlayers()
            entity.velocity = entity.location.direction.setY(0).multiply(RUNNING_MULTIPLIER)
        } else if (step == RUNNING_TIME) {
            entity.velocity = Vector()
        } else {
            val player = nearestVisiblePlayer()
            if (player == null) {
                noTargetTime += tickDelay
                entity.isAware = false
            } else {
                noTargetTime = 0
                entity.isAware = true
                entity.target = player.player
            }
        }

        if (noTargetTime >= NO_TARGET_DESPAWN_TIME) {
            ability.isSpawned = false
            step = 0
            noTargetTime = 0
            entity.remove()
        }

        step += tickDelay
    }

    private fun damageNearestPlayers(){
        game.findNearbyPlayers(entity.location, KILL_TARGET_RADIUS).forEach { it.damage() }
    }

    private fun nearestVisiblePlayer() : FnafUPlayer? {
        val visiblePlayers = HashMap<Double, FnafUPlayer>()
        for (player in game.findNearbyPlayers(entity.location, SEEK_TARGET_RADIUS)) {
            if (player.type != FnafUPlayer.Type.SURVIVOR) continue

            val targetEyeLocation = player.player.eyeLocation
            val entityEyeLocation = entity.eyeLocation
            val distance = entityEyeLocation.distance(targetEyeLocation)
            val rayTraceResult = entity.world.rayTraceEntities(entityEyeLocation, targetEyeLocation.toVector().subtract(entityEyeLocation.toVector()).normalize(),
                distance) ?: return null

            if (rayTraceResult.hitBlock != null) continue
            visiblePlayers.put(distance, player)
        }
        return visiblePlayers.minByOrNull { it.key }?.value
    }

    fun onEntityHit(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) (event.entity as Player).getFnafU()?.also { it.damage() }
        event.isCancelled = true
    }

    override fun getType(): CustomTickingEntityType<*> {
        return EntityTypes.PLUSHTRAP
    }

    override fun getTickDelay() = 2
}