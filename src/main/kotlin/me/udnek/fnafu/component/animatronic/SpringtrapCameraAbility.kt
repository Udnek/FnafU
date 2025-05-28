package me.udnek.fnafu.component.animatronic

import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.AbilityCooldown
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityHolderComponent
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityInstance
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
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

    fun fix() {
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