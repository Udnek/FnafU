package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.Resettable
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.remnanttrap.RemnantTrap

open class FreddyTrapData : MGUPlayerData, Resettable{

    companion object{
        val DEFAULT = object : FreddyTrapData(){
            override var trap: RemnantTrap?
                get() = super.trap
                set(value) {throwCanNotChangeDefault()}
            override var abilityItem: CustomItem?
                get() = super.abilityItem
                set(value) {throwCanNotChangeDefault()}
        }
    }

    open var trap: RemnantTrap? = null
    open var abilityItem: CustomItem? = null

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder, out MGUPlayerData> {
        return FnafUComponents.FREDDY_TRAP_DATA
    }

    override fun reset() {
        trap?.remove()
        abilityItem = null
    }

}