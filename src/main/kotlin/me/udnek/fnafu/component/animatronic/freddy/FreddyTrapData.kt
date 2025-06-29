package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.ability.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.trap.Trap
import me.udnek.fnafu.util.Resettable

open class FreddyTrapData : CustomComponent<MGUPlayerDataHolder>, Resettable{

    companion object{
        val DEFAULT = object : FreddyTrapData(){
            override var trap: Trap?
                get() = super.trap
                set(value) {throwCanNotChangeDefault()}
            override var abilityItem: CustomItem?
                get() = super.abilityItem
                set(value) {throwCanNotChangeDefault()}
        }
    }

    open var trap: Trap? = null
    open var abilityItem: CustomItem? = null

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder, out CustomComponent<MGUPlayerDataHolder>> {
        return FnafUComponents.FREDDY_TRAP_DATA
    }

    override fun reset() {
        trap?.remove()
    }

}