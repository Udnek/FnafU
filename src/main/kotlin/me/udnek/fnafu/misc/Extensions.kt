package me.udnek.fnafu.misc

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.mgu.MGUManager
import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.item.survivor.camera.CameraButton
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory


fun Player.getFnafU(): FnafUPlayer? {
    return MGUManager.get().getPlayer(this) as? FnafUPlayer
}

fun CustomSound.play(player: FnafUPlayer){
    this.play(player.player)
}

fun Location.setOrigin(origin: Location): Location {
    return Originable.setOrigin(this, origin)
}

fun ItemStack.getCameraId(): String? {
    return CameraButton.getCameraId(this)
}

fun ItemStack.getCustom(): CustomItem? {
    return CustomItem.get(this)
}

fun Block.getCustom(): CustomBlockType? {
    return CustomBlockType.get(this)
}

fun Location.toCenterFloor(): Location {
    val center = this.toCenterLocation()
    center.set(center.x, center.blockY.toDouble(), center.z)
    return center
}

fun List<Location>.getNearest(location: Location): Location {
    return sortedByDistance(location).first()
}

fun List<Location>.sortedByDistance(location: Location): List<Location> {
    return this.sortedBy { l -> l.distance(location) }
}

fun List<Location>.getFarthest(location: Location): Location {
    return this.sortedByDistance(location).last()
}

fun <A, B> Pair<A, B>.toApache(): org.apache.commons.lang3.tuple.Pair<A, B>{
    return org.apache.commons.lang3.tuple.Pair.of(this.first, this.second)
}

fun PlayerInventory.addToBestSlot(item: ItemStack){
    val slot = item.getData(DataComponentTypes.EQUIPPABLE)?.slot()
    if (slot != null && this.getItem(slot).isEmpty){
        this.setItem(slot, item)
    } else {
        this.addItem(item)
    }
}











