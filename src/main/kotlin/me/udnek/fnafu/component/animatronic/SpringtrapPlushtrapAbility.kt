package me.udnek.fnafu.component.animatronic

import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.entity.plushtrap.Plushtrap
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.AbilityCooldown
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityHolderComponent
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityInstance
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import org.bukkit.inventory.ItemStack


open class SpringtrapPlushtrapAbility : MGUAbilityInstance, Resettable, AbilityCooldown(20) {

    companion object {
        val DEFAULT = object : SpringtrapPlushtrapAbility(){
            override fun activate(animatronic: FnafUPlayer, plushtrapSummonItem: ItemStack) {
                throwCanNotChangeDefault()
            }
        }

    }

    private var plushtrap: Plushtrap? = null

    open fun activate(animatronic: FnafUPlayer, plushtrapSummonItem: ItemStack) {
        if (start(animatronic, plushtrapSummonItem)) return
        isActivated = true
        val location = animatronic.player.location
        location.pitch = 0f
        plushtrap = EntityTypes.PLUSHTRAP.spawnAndGet(location)
        plushtrap!!.game = animatronic.game
        plushtrap!!.ability = this
    }

    override fun getType(): CustomComponentType<out MGUAbilityHolderComponent, out CustomComponent<MGUAbilityHolderComponent>> {
        return Abilities.SPRINGTRAP_PLUSHTRAP
    }

    override fun reset() {
        if (plushtrap != null) plushtrap!!.remove()
        isActivated = false
    }
}