package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.Resettable
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.remnanttrap.RemnantTrap
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Location

open class FreddyTrapData : MGUPlayerData, Resettable{

    companion object{
        val DEFAULT = object : FreddyTrapData(){
            override var trap: RemnantTrap?
                get() = super.trap
                set(value) {throwCanNotChangeDefault()}
            override var abilityItem: CustomItem?
                get() = super.abilityItem
                set(value) {throwCanNotChangeDefault()}
            override var player: FnafUPlayer?
                get() = super.player
                set(value) {throwCanNotChangeDefault()}
            override var ability: FreddySetTrapAbility?
                get() = super.ability
                set(value) {throwCanNotChangeDefault()}
            override var teleportLocation: Location?
                get() = super.teleportLocation
                set(value) {}
        }
    }

    open var trap: RemnantTrap? = null
    open var abilityItem: CustomItem? = null
    open var player: FnafUPlayer? = null
    open var ability: FreddySetTrapAbility? = null;
    open var teleportLocation: Location? = null

    fun set(item: CustomItem, trap: RemnantTrap, player: FnafUPlayer, ability: FreddySetTrapAbility, teleportLocation: Location){
        this.abilityItem = item
        this.trap = trap
        this.player = player
        this.ability = ability
        this.teleportLocation = teleportLocation
    }

    override fun getType(): CustomComponentType<in MGUPlayerDataHolder, out CustomComponent<in MGUPlayerDataHolder>?> {
        return FnafUComponents.FREDDY_TRAP_DATA
    }

    override fun reset() {
        trap?.remove()
        trap = null
        abilityItem = null
        player = null
        ability = null
    }

}