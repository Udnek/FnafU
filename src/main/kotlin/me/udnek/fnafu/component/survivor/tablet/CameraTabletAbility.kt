package me.udnek.fnafu.component.survivor.tablet

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.lore.ability.ActiveAbilityLorePart
import me.udnek.coreu.util.Either
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

open class CameraTabletAbility(val guiColor: Color, val noiseColor: TextColor, val isCut: Boolean, val damagePerUsage: Float) : FnafUActiveAbility() {

    init {
        components.set(AttributeBasedProperty(0.5 * 20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    companion object {
        val CUT: CameraTabletAbility = CameraTabletAbility( Color.WHITE, NamedTextColor.WHITE, true, 0.06f)
        val FULL: CameraTabletAbility = CameraTabletAbility(Color.GREEN, NamedTextColor.GREEN, false, 0.03f)
    }

    override fun addPropertyLines(componentable: ActiveAbilityLorePart) {
        super.addPropertyLines(componentable)
        componentable.addAbilityStat(
            Component.translatable("ability.fnafu.camera_tablet.damage_per_usage", listOf(
                Component.text(Utils.roundToTwoDigits(damagePerUsage*100.0)))))
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
        cameras.durability -= damagePerUsage
        if (cameras.isBroken || player.game.energy.isEndedUp) {
            return ActionResult.NO_COOLDOWN
        }
        cameras.openMenu(player, event.item!!)
        val lastCamera = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_CAMERA_DATA).lastCamera
        cameras.spectateCamera(player, lastCamera ?: cameras.cameras.firstOrNull{it.isInCutMenu}!!, event.item!!)
        return ActionResult.FULL_COOLDOWN
    }
}