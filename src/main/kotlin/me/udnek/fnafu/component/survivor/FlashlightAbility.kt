package me.udnek.fnafu.component.survivor

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.BaseUniversalSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items
import me.udnek.coreu.util.FakeBlock
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Light
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable

class FlashlightAbility : FnafUActiveAbility() {

    companion object {
        val DEFAULT = FlashlightAbility()
    }

    private var task: BukkitRunnable? = null
    private val blockData: BlockData = (Material.LIGHT.createBlockData() as Light).also { it.level = 8 }
    private val finalBlock = Material.SUGAR_CANE.createBlockData() /*(Material.LIGHT.createBlockData() as Light).also { it.level = 9 }*/

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        event: PlayerInteractEvent
    ): ActionResult {
        if (event.action.isLeftClick) return ActionResult.NO_COOLDOWN
        if (task == null){
            task = object : BukkitRunnable() {
                override fun run() {
                    if (player.player.inventory.getItem((slot.left as? BaseUniversalSlot)?.equipmentSlot ?: EquipmentSlot.OFF_HAND).getCustom() != Items.FLASHLIGHT) {
                        cancel()
                        return
                    }
                    val location = player.player.eyeLocation
                    val direction = location.direction.setY(0).normalize()
                    for (i in  0..15) {
                        val previousLocation = location.clone()
                        location.add(direction)
                        val players = player.game.playerContainer.all.map { it.player }
                        if (location.block.blockData.isOccluding)  {
                            if (previousLocation.block.isEmpty)  FakeBlock().show(players, previousLocation, finalBlock, 1)
                            break
                        } else if (previousLocation.block.isEmpty) {
                            FakeBlock().show(players, previousLocation, blockData, 1)
                        }
                    }
                }
            }
            task!!.runTaskTimer(FnafU.instance, 0, 1)
        } else {
            task!!.cancel()
            task = null
        }
        return ActionResult.NO_COOLDOWN
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.FLASHLIGHT_ABILITY
    }
}
