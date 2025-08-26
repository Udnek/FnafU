package me.udnek.fnafu.block.decor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import io.papermc.paper.event.player.PlayerPickBlockEvent
import me.udnek.coreu.custom.component.instance.MiddleClickableBlock
import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.util.Either
import me.udnek.fnafu.item.Items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable

class NightstandBlock : RotatableCustomBlockType() {

    override fun getRawId(): String = "nightstand"

    override fun getItem(): CustomItem = Items.NIGHTSTAND

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(object : MiddleClickableBlock.Implementation(){
            override fun getItemForCreative(block: CustomBlockType, event: PlayerPickBlockEvent): ItemStack {
                return (block as? NightstandBlock)?.getDisplay(event.block)?.itemStack ?: super.getItemForCreative(block, event)
            }
        })
    }


    override fun placeAndReturnDisplay(location: Location, context: CustomBlockPlaceContext): ItemDisplay {
        val display = super.placeAndReturnDisplay(location, context)
        val player = context.player() ?: return display
        val dye = Nms.get().getColorByDye(player.inventory.itemInOffHand.type) ?: return display
        val item = display.itemStack
        item.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(dye.fireworkColor()))
        display.setItemStack(item)
        return display
    }

    override fun getBreakSpeedBaseBlock(): Material = Material.OAK_LOG
}