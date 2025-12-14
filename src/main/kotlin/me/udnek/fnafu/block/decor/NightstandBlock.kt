package me.udnek.fnafu.block.decor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import io.papermc.paper.event.player.PlayerPickBlockEvent
import me.udnek.coreu.custom.component.instance.MiddleClickableBlock
import me.udnek.coreu.custom.component.instance.RightClickableBlock
import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.fnafu.item.Items
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class NightstandBlock : RotatableCustomBlockType() {

    companion object{
        const val COLOR_DATA_KEY: String = "color"
    }

    override fun getRawId(): String = "nightstand"

    override fun getItem(): CustomItem = Items.NIGHTSTAND

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(object : MiddleClickableBlock.Implementation(){
            override fun getItemForCreative(block: CustomBlockType, event: PlayerPickBlockEvent): ItemStack {
                return (block as? NightstandBlock)?.getDisplay(event.block)?.itemStack ?: super.getItemForCreative(block, event)
            }
        })
        components.set(object : RightClickableBlock{
            override fun onRightClick(blockType: CustomBlockType, event: PlayerInteractEvent) {
                event.isCancelled = true
                if (event.player.gameMode != GameMode.CREATIVE) return
                val color = Nms.get().getColorByDye(event.player.inventory.itemInMainHand.type)?.fireworkColor() ?: return
                (blockType as NightstandBlock).getDisplay(event.clickedBlock!!)?.let {
                    val itemStack = it.itemStack
                    itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(color))
                    it.setItemStack(itemStack)
                }
            }
        })
    }

    override fun placeAndReturnDisplay(location: Location, context: CustomBlockPlaceContext): ItemDisplay {
        val display = super.placeAndReturnDisplay(location, context)
        val event = context.event ?: return display
        val color =
            event.player.inventory.getItem(event.hand.oppositeHand).let { Nms.get().getColorByDye(it.type)?.fireworkColor() }
            ?: event.itemInHand.getData(DataComponentTypes.DYED_COLOR)?.color()
            ?: return display
        val item = display.itemStack
        item.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(color))
        storeData(getState(location), COLOR_DATA_KEY, color.asRGB().toString())
        display.setItemStack(item)
        return display
    }

    override fun getBreakSpeedBaseBlock(): Material = Material.OAK_LOG
}