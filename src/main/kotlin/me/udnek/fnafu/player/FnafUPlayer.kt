package me.udnek.fnafu.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataValue
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedGameProfile
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import it.unimi.dsi.fastutil.ints.IntArrayList
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.mgu.player.MGUAbstractPlayer
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.util.Resettable
import me.udnek.fnafu.util.getCustom
import me.udnek.fnafu.util.getFarthest
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.skinsrestorer.api.SkinsRestorerProvider
import net.skinsrestorer.api.property.InputDataResult
import net.skinsrestorer.api.property.SkinProperty
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
import kotlin.jvm.optionals.getOrNull

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

    fun setSkin() {
        val storage = SkinsRestorerProvider.get().skinStorage
        storage.setCustomSkinData("custom", SkinProperty.of(
            "ewogICJ0aW1lc3RhbXAiIDogMTY0MTIwNjc4OTIwNywKICAicHJvZmlsZUlkIiA6ICJjZjgwY2E3NDFjNWQ0N2E3YWFjNGNmYjI2MjI0NDJmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzb21lb25lX28iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc4NGJiOTEwMDQ4MGVlMTMyNWIyY2Q4NWJkYTkxMjI1NDcwYWMwOTRlZTExNzRiMzg4MDdmNzAwZDcyZDJkYyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            "P2+tca61qcDIdKmIUgENZ0bhGzq3Y7mlGrBNpqVTMXGem8A8dBv7JaUqJqdwdFDhQOn9VExiUbPWQLbTc/OQezXxonFw2Wwq7wK1lRGPUwZIpLQxPh9JgkVPBib/vG/wgGm7qMscvkRp06vhQB1OdtFEKnPwt5T6GLfCnP5ifLPaWo9FCdr5bgO7RaozXS4hgGLjt1y87JAWZMABWuFQGPeNgnDQAlSVQTKNYosxjyl51wwDZxhHnjmW1UUqZZehQ2NlQ2G/bdp2sasf/8aWfWkLNifY01c7pNGDAtVPes5C0xAjHnCjNpiId/ylKYeb0HCM3w18N5kWPo2LULHb4R7TVgXuHBoIYHr70zx1DSutNLchh5NmTp/FhRZgkP6sucBVu6Cq1g4RP11B7vkQRZJbjAl6r0ur7pRha+ZFI6hR+k8NNqSWozree5oR7xZ7gaSKARcD9i78YNRXbDRprastLWV3iwH2SEeEV2JmgDXN+CjM6HJ0liXfz7VtRKajG8zF/9ZH3RxegbRxiqzs+CUkJnHtxKuDYjfScW6uFflvh8/Wf//xEulzxEgdAZdXzBgwPv3U8uXgfN1qHP0SAVaivZPL5g7e0hDTdrXFbUA6+n6PTssuwf52gLGdMaHJ0AOdrlgXxDSFb7LXEg+bWv8lFs34SlVFyCmZFEOLvZU="
        ))
        val skin = storage.findOrCreateSkinData("custom")
        SkinsRestorerProvider.get().playerStorage.setSkinIdOfPlayer(player.uniqueId, skin.get().identifier)
        SkinsRestorerProvider.get().getSkinApplier(player.javaClass).applySkin(player)
    }

    fun setUp() = kit.setUp(this)

    fun damage() {
        if (type != Type.SURVIVOR) return
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
        status = Status.ALIVE
        player.inventory.clear()
        player.clearActivePotionEffects()
        player.getAttribute(Attribute.JUMP_STRENGTH)!!.removeModifier(NamespacedKey(FnafU.instance, "game_js"))
        data.forEach { (it as? Resettable)?.reset() }
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








