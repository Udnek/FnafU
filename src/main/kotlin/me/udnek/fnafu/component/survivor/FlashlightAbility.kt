package me.udnek.fnafu.component.survivor

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.misc.FakeBlock
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Light
import org.bukkit.event.player.PlayerInteractEvent
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
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        if (task == null){
            task = object : BukkitRunnable() {
                override fun run() {
                    val location = player.player.eyeLocation
                    val direction = location.direction.setY(0).normalize()
                    if (location.block.isSolid) return
                    repeat(15) {
                        FakeBlock(player.game.playerContainer.all, location, blockData, 1)
                        location.add(direction)
                        if (location.block.isSolid) {
                            FakeBlock(player.game.playerContainer.all, location, finalBlock, 1)
                            return@repeat
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

    override fun getType(): CustomComponentType<out RPGUActiveAbilityItem?, out CustomComponent<RPGUActiveAbilityItem>> {
        return FnafUComponents.FLASHLIGHT_ABILITY
    }
}
