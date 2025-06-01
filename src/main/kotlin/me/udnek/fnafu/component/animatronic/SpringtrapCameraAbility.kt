package me.udnek.fnafu.component.animatronic

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.ability.MGUAbilityHolderComponent
import me.udnek.coreu.mgu.ability.MGUAbilityInstance
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.AbilityCooldown
import me.udnek.fnafu.util.Resettable
import org.bukkit.inventory.ItemStack


open class SpringtrapCameraAbility : MGUAbilityInstance, AbilityCooldown(20), Resettable{

    companion object {
        val DEFAULT = object : SpringtrapCameraAbility(){
            override fun activate(animatronic: FnafUPlayer, item: ItemStack) {
                throwCanNotChangeDefault()
            }
        }

    }

    open fun activate(animatronic: FnafUPlayer, item: ItemStack) {
        if (start(animatronic, item)) return
        val systems = animatronic.game.systems
        if (systems.camera.isBroken()) {
            setInfiniteCooldown()
            return
        }
        systems.destroySystem(systems.camera)
    }

    fun repaired() {
        isActivated = false
        setBaseCooldown()
    }

    override fun getType(): CustomComponentType<out MGUAbilityHolderComponent, out CustomComponent<MGUAbilityHolderComponent>> {
        return Abilities.SPRINGTRAP_CAMERA
    }

    override fun reset() {
        setCooldown(0)
    }
}