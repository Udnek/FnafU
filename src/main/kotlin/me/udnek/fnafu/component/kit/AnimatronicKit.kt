package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import net.skinsrestorer.api.SkinsRestorerProvider
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class AnimatronicKit : ConstructableKit {

    private val skinHash: String
    override var jumpScareSound: CustomSound?

    constructor(id: String, type: FnafUPlayer.Type, displayItem: CustomItem, customItems: List<CustomItem>, skinHash: String, jumpScareSound: CustomSound)
            : super(id, type, displayItem, customItems) {
        this.skinHash = skinHash
        this.jumpScareSound = jumpScareSound
    }

    override fun setUp(player: FnafUPlayer) {
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        val result = skinsRestorerAPI.skinStorage.findOrCreateSkinData("https://textures.minecraft.net/texture/$skinHash")
        skinsRestorerAPI.playerStorage.setSkinIdOfPlayer(player.player.uniqueId, result.get().identifier)
        skinsRestorerAPI.getSkinApplier(Player::class.java).applySkin(player.player)

        object : BukkitRunnable(){
            override fun run() { super@AnimatronicKit.setUp(player) }
        }.runTaskLater(FnafU.instance, 5)
    }
}