package me.udnek.fnafu.component.animatronic.springtrap

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.entity.plushtrap.Plushtrap
import me.udnek.fnafu.player.FnafUPlayer
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent


class SpringtrapPlushtrapAbility : FnafUActiveAbility{

    companion object {
        val DEFAULT = SpringtrapPlushtrapAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(10.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    fun getPlushtrap(player: FnafUPlayer) : Plushtrap? {
        return player.data.getOrDefault(FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA).plushtrap
    }
    fun setPlushtrap(player: FnafUPlayer, plushtrap: Plushtrap, item: CustomItem) {
        player.data.getOrCreateDefault(FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA).let {
            it.plushtrap = plushtrap
            it.abilityItem = item
        }
    }
    fun spawnPlushtrap(item: CustomItem, player: FnafUPlayer) {
        val location = player.player.location
        location.pitch = 0f
        val plushtrap = EntityTypes.PLUSHTRAP.spawnAndGet(location)
        plushtrap.owner = player
        plushtrap.game = player.game
        plushtrap.initialDirection = player.player.location.direction
        setPlushtrap(player, plushtrap, item)
        player.team?.addEntity(plushtrap.real)
    }

    fun pickPlushtrap(customItem: CustomItem, player: FnafUPlayer) {
        val plushtrap = getPlushtrap(player) ?: return
        if (customItem.hasCooldown(player.player)) plushtrap.remove()
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        event: PlayerInteractEvent
    ): ActionResult {
        val plushtrap = getPlushtrap(player)
        if (plushtrap != null) return ActionResult.INFINITE_COOLDOWN
        spawnPlushtrap(item, player)
        return ActionResult.INFINITE_COOLDOWN
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?>? {
        return Pair.of(listOf(
            "Summon plushtrap that runs straight forward,",
            "then starts to persuade nearest survivor"),
            listOf(
            "Призывает плюштрапа, который бежит по прямой,",
            "через некторое время начинает приследовать ближайшего выжившего"))
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.SPRINGTRAP_PLUSHTRAP_ABILITY
    }
}