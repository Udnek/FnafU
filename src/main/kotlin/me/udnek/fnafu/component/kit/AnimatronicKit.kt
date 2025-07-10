package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import net.skinsrestorer.api.SkinsRestorerProvider
import net.skinsrestorer.api.property.SkinProperty
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable


class AnimatronicKit : ConstructableKit {

    override var jumpScareSound: CustomSound?

    constructor(id: String, type: FnafUPlayer.Type, displayItem: CustomItem, customItems: List<CustomItem>, jumpScareSound: CustomSound, value: String,
                signature: String) : super(id, type, displayItem, customItems) {
        this.jumpScareSound = jumpScareSound
        SkinsRestorerProvider.get().skinStorage.setCustomSkinData(id, SkinProperty.of(value, signature))
    }

    override fun setUp(player: FnafUPlayer) {
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        skinsRestorerAPI.playerStorage.setSkinIdOfPlayer(player.player.uniqueId, skinsRestorerAPI.skinStorage.findOrCreateSkinData(rawId).get().identifier)
        skinsRestorerAPI.getSkinApplier(Player::class.java).applySkin(player.player)

        object : BukkitRunnable(){
            override fun run() { super@AnimatronicKit.setUp(player) }
        }.runTaskLater(FnafU.instance, 5)
    }
}