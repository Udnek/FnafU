package me.udnek.fnafu.component.survivor

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.inventory.ItemStack

open class CurrentInventoryData: MGUPlayerData {

    protected var mutableItems: MutableList<ItemStack> = ArrayList()
    val items: List<ItemStack>
        get() = mutableItems

    companion object{
        val DEFAULT = object : CurrentInventoryData(){
            override fun replaceFirstItem(old: ItemStack, new: ItemStack) = throwCanNotChangeDefault()
            override fun add(item: ItemStack) = throwCanNotChangeDefault()
        }
    }
    open fun add(item: ItemStack){
        mutableItems.add(item)
    }

    open fun replaceFirstItem(old: ItemStack, new: ItemStack){
        val index = mutableItems.indexOf(old)
        if (index != -1) mutableItems[index] = new
    }

    fun give(player: FnafUPlayer){
        for (stack in mutableItems) { player.player.inventory.addItem(stack)}
    }

    override fun reset() {
        mutableItems = ArrayList()
    }

    override fun getType(): CustomComponentType<in MGUPlayerDataHolder, out CustomComponent<in MGUPlayerDataHolder>?> {
        return FnafUComponents.CURRENT_INVENTORY_DATA
    }
}