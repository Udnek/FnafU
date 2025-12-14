package me.udnek.fnafu.component.survivor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.BaseUniversalSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUToggleItem
import me.udnek.coreu.rpgu.component.ability.toggle.RPGUConstructableToggleAbility
import me.udnek.coreu.util.Either
import me.udnek.coreu.util.FakeBlock
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.sound.Sounds
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Light
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class FlashlightAbility : RPGUConstructableToggleAbility() {

    companion object {
        val DEFAULT = FlashlightAbility()
        private val beam: BlockData = (Material.LIGHT.createBlockData() as Light).also { it.level = 8 }
        private val beamCollision: BlockData = (Material.LIGHT.createBlockData() as Light).also { it.level = 9 }
    }

    override fun setToggled(customItem: CustomItem, player: Player, slot: BaseUniversalSlot, toggle: Boolean): Boolean {
        if (toggle) {
            slot.modifyItem({ it.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents())}, player)
        } else {
            slot.modifyItem( { it.unsetData(DataComponentTypes.BUNDLE_CONTENTS) }, player)
        }
        Sounds.FLASHLIGHT_CLICK.play(player.location)
        player.getFnafU()?.currentInventory?.replaceByType(slot.getItem(player)!!)
        return super.setToggled(customItem, player, slot, toggle)
    }

    override fun action(
        item: CustomItem,
        entity: LivingEntity,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        p3: Int
    ): ActionResult {
        val player = (entity as? Player)?.getFnafU() ?: return ActionResult.NO_COOLDOWN
        val location = player.player.eyeLocation
        val direction = location.direction.normalize()
        val players = player.game.playerContainer.all.map { it.player }
        for (i in  0..15) {
            val previousLocation = location.clone()
            location.add(direction)
            if (location.block.blockData.isOccluding)  {
                if (previousLocation.block.isEmpty) {
                    FakeBlock.show(previousLocation, beamCollision, players, 1)
                }
                break
            } else if (previousLocation.block.isEmpty) {
                FakeBlock.show(previousLocation, beam, players, 1)
            }
        }
        return ActionResult.FULL_COOLDOWN
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Toggles flashlight that can be recharged at generator") to listOf("Переключает фонарик, который заряжается у генератора")).toApache()
    }

    override fun getSlot(): CustomEquipmentSlot = CustomEquipmentSlot.OFF_HAND

    override fun getType(): CustomComponentType<in RPGUToggleItem, out CustomComponent<in RPGUToggleItem>?> {
        return FnafUComponents.FLASHLIGHT_ABILITY
    }
}
