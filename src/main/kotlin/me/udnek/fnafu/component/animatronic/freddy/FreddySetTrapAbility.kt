package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent


open class FreddySetTrapAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = FreddySetTrapAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(30.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
        components.set(AttributeBasedProperty(5.0, RPGUComponents.ABILITY_AREA_OF_EFFECT))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        event: PlayerInteractEvent
    ): ActionResult {
        val location = player.player.location
        location.pitch = 0f
        location.yaw = 0f
        location.add(0.0, 0.2, 0.0)

        val data = player.data.getOrCreateDefault(FnafUComponents.FREDDY_TRAP_DATA)
        data.trap?.remove()
        val trap = EntityTypes.REMNANT_TRAP.spawnAndGet(location)

        data.set(item, trap, player, this, location)

        trap.data = data

        player.game.playerContainer.survivors.forEach {
            Nms.get().sendFakeDestroyEntities(listOf(trap.real), it.player)
        }
        return ActionResult.FULL_COOLDOWN
    }


    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.FREDDY_SET_TRAP_ABILITY
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Sets trap that reveals nearby players") to listOf("Устанавливает ловушку, котороя раскрывает игроков неподалёку")).toApache()
    }
}





