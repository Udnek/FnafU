package me.udnek.fnafu.player

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.mgu.Resettable
import me.udnek.coreu.mgu.player.MGUAbstractPlayer
import me.udnek.coreu.util.FakeGlow
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.component.survivor.CurrentInventoryData
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.getFarthest
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.skinsrestorer.api.SkinsRestorerProvider
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import org.bukkit.util.Vector
import java.time.Duration

class FnafUPlayer(private val player: Player, val type: Type, private val game: FnafUGame) : MGUAbstractPlayer(player, game), Resettable {

    var status: Status = Status.ALIVE

    var kit: Kit
        set(value) = data.set(value)
        get() = data.getOrDefault(FnafUComponents.KIT)

    var currentInventory: CurrentInventoryData
        set(value) = data.set(value)
        get() = data.getOrCreateDefault(FnafUComponents.CURRENT_INVENTORY_DATA)

    val team: Team?
        get() = game.getTeam(this)

    val abilityItems: List<CustomItem>
        get() {
            val items = ArrayList(kit.permanentItems.mapNotNull { stack -> stack.getCustom() })
            items.addAll(currentInventory.items.mapNotNull { stack -> stack.getCustom() })
            return items
        }

    override fun getGame(): FnafUGame = game

    override fun toString(): String = "[" + type + "] " + player.name

    fun regiveInventory(){
        player.inventory.clear()
        kit.regive(this)
        currentInventory.give(this)
    }

    fun teleport(locationData: LocationData, noiseColor: TextColor? = null) = teleport(locationData.random, noiseColor)

    fun teleport(location: Location, noiseColor: TextColor? = null) {
        if (noiseColor != null) showNoise(noiseColor)
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

    fun playSound(location: Location, sound: CustomSound, range: Float) = sound.play(location, player, range/16f)

    fun sendPacket(packetContainer: PacketContainer) = ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer)

    fun showAuraTo(toPlayers: List<FnafUPlayer>, duration: Int) = FakeGlow(toPlayers.map { it.player }, player).glow(duration)

    fun showNoise(color: TextColor){
        player.showTitle(Title.title(Component.text("1").font(Key.key("fnafu:system/camera")).color(color), Component.empty(),
            Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(200), Duration.ofMillis(200))))
    }

    fun saveSkin() {
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        val skin = skinsRestorerAPI.playerStorage.getSkinForPlayer(player.uniqueId, player.name).get()
        skinsRestorerAPI.skinStorage.setCustomSkinData(player.name, skin)
    }

    fun clearSkin() {
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        skinsRestorerAPI.playerStorage.setSkinIdOfPlayer(player.uniqueId, skinsRestorerAPI.skinStorage.findSkinData(player.name).get().identifier)
        skinsRestorerAPI.getSkinApplier(Player::class.java).applySkin(player.player)
    }

    fun damage(damageSound: CustomSound) {
        if (type != Type.SURVIVOR) return
        if (status != Status.ALIVE) return
        damageSound.play(player.location)
        if (game.survivorLives == 0){
            this.die()
            return
        }
        player.velocity = Vector()
        teleport((game.map.getLocation(LocationType.RESPAWN_SURVIVOR)!!).all.getFarthest(player.location))
        player.inventory.clear()
        currentInventory.reset()
        kit.regiveCurrentInventory(this)
        kit.regive(this)
        currentInventory.give(this)
        game.survivorLives -= 1
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








