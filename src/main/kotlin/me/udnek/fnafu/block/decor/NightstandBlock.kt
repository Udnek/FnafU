package me.udnek.fnafu.block.decor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.util.Either
import me.udnek.fnafu.item.Items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.TileState
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable

class NightstandBlock : RotatableCustomBlockType() {
    override fun getFakeDisplay(): ItemStack = Items.NIGHTSTAND.item

    override fun getLoot(): Either<LootTable?, List<ItemStack?>?>? = null

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

    override fun getRawId(): String = "nightstand"

    override fun load(p0: TileState) {}

    override fun unload(p0: TileState) {}

}