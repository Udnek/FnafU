package me.udnek.fnafu.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import it.unimi.dsi.fastutil.ints.IntArrayList
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Components
import me.udnek.fnafu.component.Kit
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.customminigame.player.MGUAbstractPlayer
import me.udnek.itemscoreu.customsound.CustomSound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import java.util.*

class FnafUPlayer(private val player: Player, val type: PlayerType, private val game: FnafUGame) : MGUAbstractPlayer(player, game), Resettable {

    override fun getGame(): FnafUGame = game

    override fun toString(): String = "[" + type + "] " + player.name

    var kit: Kit
        set(value) = components.set(value)
        get() = components.getOrDefault(Components.KIT)



    fun teleport(locationData: LocationData) { teleport(locationData.random) }

    fun teleport(location: Location) { player.teleport(location) }

    fun showTitle(title: Component, subtitle: Component, fadeIn: Int, stay: Int, fadeOut: Int) {
        val titleData = Title.title(
            title,
            subtitle,
            Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
            )
        )
        player.showTitle(titleData)
    }

    fun playSound(location: Location, sound: CustomSound, range: Float) = sound.play(location, player, range/16f)

    fun sendPacket(packetContainer: PacketContainer) = ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer)

    fun showAuraTo(toPlayers: List<FnafUPlayer>, duration: Int, color: Color) {
        val protocolManager = ProtocolLibrary.getProtocolManager()

        // PACKET SPAWN

        val spawnPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY)
        spawnPacket.modifier.writeDefaults()
        spawnPacket.entityTypeModifier.write(0, EntityType.ITEM_DISPLAY)
        spawnPacket.uuiDs.write(0, UUID.randomUUID())
        spawnPacket.integers.write(1, 1)
        val location = player.location
        spawnPacket.doubles.write(0, location.x)
        spawnPacket.doubles.write(1, location.y)
        spawnPacket.doubles.write(2, location.z)

        // PACKET METADATA

        val metadataPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA) // metadata packet
        metadataPacket.integers.write(0, spawnPacket.integers.read(0)) //Set entity id from packet above
        val realEntity = Bukkit.getWorld("world")!!.spawnEntity(location, EntityType.ITEM_DISPLAY) as ItemDisplay
        realEntity.setItemStack(ItemStack(Material.DIAMOND_SWORD))
        realEntity.glowColorOverride = color
        realEntity.billboard = Display.Billboard.CENTER

        val watcher = WrappedDataWatcher.getEntityWatcher(realEntity).deepClone()
        realEntity.remove()
        watcher.setObject(
            0,
            WrappedDataWatcher.Registry.get(java.lang.Byte::class.java),
            (0x40).toByte()
        ) //Set status to glowing, found on protocol page
        val wrappedDataValueList: MutableList<WrappedDataValue> = ArrayList()
        watcher.watchableObjects.filterNotNull().forEach {
            entry: WrappedWatchableObject ->
                val dataWatcherObject = entry.watcherObject
                wrappedDataValueList.add(
                    WrappedDataValue(
                        dataWatcherObject.index,
                        dataWatcherObject.serializer,
                        entry.rawValue
                    )
                )
            }
        metadataPacket.dataValueCollectionModifier.write(0, wrappedDataValueList)

        // PACKET MOUNT

        val mountPacket = protocolManager.createPacket(PacketType.Play.Server.MOUNT)
        mountPacket.integers
            .write(0, player.entityId)
        mountPacket.integerArrays.write(0, intArrayOf(spawnPacket.integers.read(0)))

        for (toPlayer in toPlayers) {
            protocolManager.sendServerPacket(toPlayer.player, spawnPacket)
            protocolManager.sendServerPacket(toPlayer.player, metadataPacket)
            protocolManager.sendServerPacket(toPlayer.player, mountPacket)
        }

        object : BukkitRunnable() {
            override fun run() {
                player.sendMessage("TODO REMOVE ENTITY")

                val removePacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY)
                removePacket.modifier.write(0, IntArrayList(intArrayOf(spawnPacket.integers.read(0))))

                for (toPlayer in toPlayers) {
                    protocolManager.sendServerPacket(toPlayer.player, removePacket)
                }
            }
        }.runTaskLater(FnafU.instance, duration.toLong())
    }

    fun setUp(){
        kit.setUp(this)
        player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, PotionEffect.INFINITE_DURATION, 10, false, false, false))
        player.getAttribute(Attribute.JUMP_STRENGTH)!!.addModifier(
            AttributeModifier(NamespacedKey(FnafU.instance, "game_js"), -10.0, AttributeModifier.Operation.ADD_NUMBER))
    }

    override fun reset() {
        player.inventory.clear()
        player.clearActivePotionEffects()
        player.getAttribute(Attribute.JUMP_STRENGTH)!!.removeModifier(NamespacedKey(FnafU.instance, "game_js"))
    }

    enum class PlayerType {
        SURVIVOR,
        ANIMATRONIC
    }

}








