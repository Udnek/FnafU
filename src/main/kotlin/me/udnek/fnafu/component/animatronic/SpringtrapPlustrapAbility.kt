package me.udnek.fnafu.component.animatronic

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityHolderComponent
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityInstance
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import org.bukkit.entity.Drowned
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitRunnable

open class SpringtrapPlustrapAbility : MGUAbilityInstance {

    companion object {
        const val RUNNING_TIME: Int = 20 * 5
        const val RUNNING_MULTIPLIER: Float = 0.2f
        const val SEEK_TARGET_RADIUS: Float = 20f
        const val NO_TARGET_DESPAWN_TIME: Int = 20 * 10

        val DEFAULT = object : SpringtrapPlustrapAbility(){
            override fun activate(animatronic: FnafUPlayer) {
                throwCanNotChangeDefault()
            }
        }
    }

    private var plushtrap: Drowned? = null
    val isSpawned: Boolean
        get() = plushtrap != null && !plushtrap!!.isDead

    private fun spawnPlushtrap(animatronic: FnafUPlayer) {
        val location = animatronic.player.location
        location.pitch = 0f
        plushtrap = location.world.spawnEntity(location, EntityType.DROWNED) as Drowned
        plushtrap!!.isAware = false
        plushtrap!!.setBaby()
        runPlushtrap(animatronic)
        object : BukkitRunnable() {
            override fun run() {
                glowPlushtrap(animatronic)
            }
        }.runTaskLater(FnafU.instance, 20)
    }

    open fun activate(animatronic: FnafUPlayer) {
        if (isSpawned) {
            animatronic.player.sendMessage("ALREADY SPAWNED")
            return
        }

        spawnPlushtrap(animatronic)
    }

    private fun runPlushtrap(animatronic: FnafUPlayer) {
        val direction = animatronic.player.location.direction.setY(0).multiply(RUNNING_MULTIPLIER)
        object : BukkitRunnable() {
            var runningTicks: Int = 0
            override fun run() {
                if (!isSpawned) {
                    cancel()
                    return
                }
                if (runningTicks >= RUNNING_TIME) {
                    cancel()
                    mainLifeCycle(animatronic)
                    return
                }
                runningTicks++
                plushtrap!!.velocity = direction
            }
        }.runTaskTimer(FnafU.instance, 1, 1)
    }


    private fun mainLifeCycle(animatronic: FnafUPlayer) {
        val plushtrap: Drowned = plushtrap!!
        plushtrap.setRotation(plushtrap.yaw + 180f, 0f)
        object : BukkitRunnable() {
            var target: FnafUPlayer? = null
            var noTargetTicks: Int = 0

            override fun run() {
                if (!isSpawned) {
                    cancel()
                    return
                }

                if (noTargetTicks > NO_TARGET_DESPAWN_TIME){
                    despawn(plushtrap)
                }

                if (target == null) {
                    noTargetTicks++
                    target = findTarget(animatronic)
                    return
                } else {
                    noTargetTicks = 0
                }

                if (!canSee(target!!)) {
                    target = null
                    return
                }

                plushtrap.target = target!!.player
            }
        }.runTaskTimer(FnafU.instance, 0, 1)
    }

    private fun despawn(plushtrap: Drowned){
        plushtrap.remove()
    }

    private fun canSee(target: FnafUPlayer): Boolean {
        val location = plushtrap!!.location
        val rayTraceResult =
            location.world.rayTraceBlocks(location, location.direction, location.distance(target.player.location))
                ?: return true
        return (rayTraceResult.hitBlock == null)
    }

    private fun findTarget(animatronic: FnafUPlayer): FnafUPlayer? {
        val location = plushtrap!!.location
        val game = animatronic.getGame()
        val nearbyPlayers = game.findNearbyPlayers(location, SEEK_TARGET_RADIUS)

        var target: FnafUPlayer? = null
        var closestDistance = Double.POSITIVE_INFINITY
        for (nearbyPlayer in nearbyPlayers) {
            if (nearbyPlayer.type != FnafUPlayer.Type.SURVIVOR) continue
            val distance = location.distance(nearbyPlayer.player.location)
            if (distance < closestDistance && canSee(nearbyPlayer)) {
                target = nearbyPlayer
                closestDistance = distance
            }
        }
        return target
    }

    private fun glowPlushtrap(animatronic: FnafUPlayer) {
        val protocolManager = ProtocolLibrary.getProtocolManager()

        val packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA)
        val watcher = WrappedDataWatcher.getEntityWatcher(plushtrap)
        watcher.setObject(0, WrappedDataWatcher.Registry.get(java.lang.Byte::class.java), (0x40).toByte())

        val wrappedDataValueList: MutableList<WrappedDataValue> = ArrayList()
        watcher.watchableObjects.filterNotNull()
            .forEach { entry: WrappedWatchableObject ->
                val dataWatcherObject = entry.watcherObject
                wrappedDataValueList.add(
                    WrappedDataValue(
                        dataWatcherObject.index,
                        dataWatcherObject.serializer,
                        entry.rawValue
                    )
                )
            }

        packet.dataValueCollectionModifier.write(0, wrappedDataValueList)

        animatronic.sendPacket(packet)
    }

    override fun getType(): CustomComponentType<out MGUAbilityHolderComponent, out CustomComponent<MGUAbilityHolderComponent>> {
        return Abilities.SPECTATE_ENTITY
    }
}



















