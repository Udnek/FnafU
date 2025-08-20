package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.constructabletype.AbstractCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.item.Items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockState
import org.bukkit.block.Campfire
import org.bukkit.block.TileState
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable

class UniversalSlabBlock : AbstractCustomBlockType(){
    companion object {
        val STATE = Material.CAMPFIRE.createBlockData().createBlockState() as Campfire
    }

    override fun place(location: Location, context: CustomBlockPlaceContext) {
        super.place(location, context)

        val data = location.block.blockData as org.bukkit.block.data.type.Campfire
        data.isLit = false
        data.facing = context.player?.facing ?: BlockFace.NORTH
        location.block.blockData = data

        val campfire = location.block.state as Campfire
        var item : ItemStack? = context.event?.let {
            return@let context.player?.inventory?.getItem(it.hand.oppositeHand)
        }
        if (item == null || item.isEmpty) {
            val type = location.clone().add(0.0, -1.0, 0.0).block.type
            if (type.isItem) item = ItemStack(type)
        }

        campfire.setItem(0, item)
        campfire.update()
    }

    override fun getItem(): CustomItem = Items.UNIVERSAL_SLAB

    override fun getRealState(): TileState = STATE

    override fun getParticleBase(): ItemStack? = null

    override fun getLoot(): Either<LootTable?, List<ItemStack?>?>? = null

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "universal_slab"

    override fun getFakeState(): BlockState? = null

    override fun onDamage(p0: BlockDamageEvent){}

    override fun doCustomBreakTimeAndAnimation(): Boolean = false

    override fun load(p0: TileState) {}

    override fun unload(p0: TileState) {}

}
