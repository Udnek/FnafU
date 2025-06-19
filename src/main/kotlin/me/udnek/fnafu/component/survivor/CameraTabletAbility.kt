package me.udnek.fnafu.component.survivor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.util.Either
import me.udnek.coreu.util.LoreBuilder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

open class CameraTabletAbility : FnafUActiveAbility {
    val startCameraID: String
    val guiColor: Color
    val noiseColor: TextColor
    val isCut: Boolean


    companion object {
        val CUT: CameraTabletAbility = CameraTabletAbility("main_entrance", Color.WHITE, NamedTextColor.WHITE, true)
        val FULL: CameraTabletAbility = CameraTabletAbility("main_entrance", Color.GREEN, NamedTextColor.GREEN, false)
    }


    constructor(startCameraID: String, guiColor: Color, noiseColor: TextColor, isCut: Boolean){
        this.startCameraID = startCameraID
        this.guiColor = guiColor
        this.noiseColor = noiseColor
        this.isCut = isCut
    }


    open fun getOverlay() : ItemStack {
        val item = Items.CAMERA_OVERLAY.item
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addColor(guiColor))
        return item
    }

    override fun getType(): CustomComponentType<out RPGUActiveAbilityItem?, out CustomComponent<RPGUActiveAbilityItem>> {
        return FnafUComponents.CAMERA_TABLET_ABILITY
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        val cameras = player.game.systems.camera
        if (cameras.isBroken) {
            player.showNoise(noiseColor)
            return ActionResult.NO_COOLDOWN
        }
        cameras.openMenu(player, event.item!!)
        cameras.spectateCamera(player, startCameraID, event.item!!)
        return ActionResult.FULL_COOLDOWN
    }

}