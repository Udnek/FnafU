package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.misc.AnimatronicSkin
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.scheduler.BukkitRunnable


class AnimatronicKit : ConstructableKit {

    private val animatronicSkin: AnimatronicSkin
    override var jumpScareSound: CustomSound?

    constructor(id: String, type: FnafUPlayer.Type, translation: TranslatableThing, displayItem: CustomItem, customItems: List<CustomItem>, jumpScareSound: CustomSound,
                animatronicSkin: AnimatronicSkin) : super(id, type, translation,displayItem, customItems) {
        this.jumpScareSound = jumpScareSound
        this.animatronicSkin = animatronicSkin
    }

    override fun setUp(player: FnafUPlayer) {
        super.setUp(player)
        animatronicSkin.setSkin(player)
    }
}