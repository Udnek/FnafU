package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.fnafu.player.FnafUPlayer
import net.skinsrestorer.api.SkinsRestorerProvider
import org.bukkit.entity.Player

class AnimatronicKit : ConstructableKit {

    private val skinHash: String

    constructor(id: String, type: FnafUPlayer.Type, displayItem: CustomItem, customItems: List<CustomItem>, skinHash: String)
            : super(id, type, displayItem, customItems) {
        this.skinHash = skinHash
    }

    override fun setUp(player: FnafUPlayer) {
        super.setUp(player)
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        val result = skinsRestorerAPI.skinStorage.findOrCreateSkinData("https://textures.minecraft.net/texture/$skinHash")
        skinsRestorerAPI.playerStorage.setSkinIdOfPlayer(player.player.uniqueId, result.get().identifier)
        skinsRestorerAPI.getSkinApplier(Player::class.java).applySkin(player.player)
    }
}