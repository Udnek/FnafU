package me.udnek.fnafu.component

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.map.FnafUMap

open class KitStageData : MGUPlayerData  {

    open var isReady: Boolean = false
    open var chosenKit: Kit = Kit.REGISTRY.all.first()
    open var chosenMap: FnafUMap? = null

    companion object{
        val DEFAULT = object : KitStageData(){
            override var isReady: Boolean
                get() = super.isReady
                set(value) = throwCanNotChangeDefault()
            override var chosenMap: FnafUMap?
                get() = super.chosenMap
                set(value) = throwCanNotChangeDefault()
            override var chosenKit: Kit
                get() = super.chosenKit
                set(value) = throwCanNotChangeDefault()
        }
    }

    override fun reset() {
        isReady = false
    }

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder, out CustomComponent<MGUPlayerDataHolder>> {
        return FnafUComponents.KIT_STAGE_DATA
    }
}