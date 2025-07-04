package me.udnek.fnafu.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import it.unimi.dsi.fastutil.ints.IntArrayList
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.mgu.Resettable
import me.udnek.coreu.mgu.player.MGUAbstractPlayer
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.util.getCustom
import me.udnek.fnafu.util.getFarthest
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.skinsrestorer.api.SkinsRestorerProvider
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Team
import java.lang.Byte
import java.time.Duration
import java.util.*
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.intArrayOf

class FnafUPlayer(private val player: Player, val type: Type, private val game: FnafUGame) : MGUAbstractPlayer(player, game), Resettable {

    var status: Status = Status.ALIVE

    var kit: Kit
        set(value) = data.set(value)
        get() = data.getOrDefault(FnafUComponents.KIT)


    val abilityItems: List<CustomItem>
        get() = kit.items.mapNotNull { stack -> stack.getCustom() }

    override fun getGame(): FnafUGame = game

    override fun toString(): String = "[" + type + "] " + player.name

    fun teleport(locationData: LocationData) = teleport(locationData.random, null)

    fun teleport(location: Location) = teleport(location, null)

    fun teleport(locationData: LocationData, maskColor: TextColor?) = teleport(locationData.random, maskColor)

    fun teleport(location: Location, maskColor: TextColor?) {
        if (maskColor != null) showNoise(maskColor)
        player.teleport(location)
    }

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

    fun getTeam(): Team? = game.getTeam(this)

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
            WrappedDataWatcher.Registry.get(Byte::class.java),
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

    fun showNoise(color: TextColor){
        player.showTitle(Title.title(Component.text("3").font(Key.key("fnafu:camera")).color(color), Component.empty(),
            Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(200), Duration.ofMillis(200))))
    }

    fun clearSkin() {
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        val result = skinsRestorerAPI.skinStorage.findOrCreateSkinData(player.name)
        skinsRestorerAPI.playerStorage.setSkinIdOfPlayer(player.uniqueId, result.get().identifier)
        skinsRestorerAPI.getSkinApplier(Player::class.java).applySkin(player)
    }

    fun damage(damageSound: CustomSound) {
        if (type != Type.SURVIVOR) return
        if (status != Status.ALIVE) return
        damageSound.play(player.location)
        if (game.survivorLives == 0){
            this.die()
            return
        }
        teleport((game.map.getLocation(LocationType.RESPAWN_SURVIVOR)!!).all.getFarthest(player.location))
        game.survivorLives -= 1
        game.updateSurvivorLives()
    }

    fun die() {
        status = Status.DEAD
        player.gameMode = GameMode.SPECTATOR
        game.checkForEndConditions()
    }

    override fun reset() {
        super.reset()
        status = Status.ALIVE
        player.inventory.clear()
        player.clearActivePotionEffects()
        player.getAttribute(Attribute.JUMP_STRENGTH)!!.removeModifier(NamespacedKey(FnafU.instance, "game_js"))
    }

    enum class Type {
        SURVIVOR,
        ANIMATRONIC
    }

    enum class Status {
        ALIVE,
        DEAD
    }

}








