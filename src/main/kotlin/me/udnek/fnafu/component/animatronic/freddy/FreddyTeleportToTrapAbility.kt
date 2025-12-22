package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.misc.Utils
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent


open class FreddyTeleportToTrapAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = FreddyTeleportToTrapAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(10.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: UniversalInventorySlot,
        event: PlayerInteractEvent
    ): ActionResult {
        Utils.freddySpawnDustParticle(player.player.location)
        val teleportLocation = player.data.getOrDefault(FnafUComponents.FREDDY_TRAP_DATA).teleportLocation ?: return ActionResult.NO_COOLDOWN
        teleportLocation.yaw = player.player.location.yaw
        teleportLocation.pitch = player.player.location.pitch
        player.teleport(teleportLocation)
        Utils.freddySpawnDustParticle(teleportLocation)
        Sounds.FREDDY_LAUGH.play(player.player.location)
        return ActionResult.FULL_COOLDOWN
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Teleports to trap") to listOf("Телепортируетесь к ловушку")).toApache()
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.FREDDY_TELEPORT_TO_TRAP_ABILITY
    }
}





