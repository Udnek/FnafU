package me.udnek.fnafu.component.animatronic.springtrap

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.plushtrap.Plushtrap

open class SpringtrapPlushtrapData : MGUPlayerData {

    companion object{
        val DEFAULT = object : SpringtrapPlushtrapData(){
            override var plushtrap: Plushtrap?
                get() = super.plushtrap
                set(value) {throwCanNotChangeDefault()}
            override var abilityItem: CustomItem?
                get() = super.abilityItem
                set(value) {throwCanNotChangeDefault()}
        }
    }

    open var plushtrap: Plushtrap? = null
    open var abilityItem: CustomItem? = null

    override fun getType(): CustomComponentType<in MGUPlayerDataHolder, out CustomComponent<in MGUPlayerDataHolder>?> {
        return FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA
    }

    override fun reset() {
        plushtrap?.remove()
        abilityItem = null
    }

}