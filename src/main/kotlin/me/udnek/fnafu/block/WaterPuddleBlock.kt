package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.constructabletype.AbstractCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.fnafu.item.Items
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.block.Sign
import org.bukkit.block.TileState
import org.bukkit.block.data.Levelled
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.inventory.ItemStack

class WaterPuddleBlock : AbstractCustomBlockType() {

    companion object{
        private val STATE: Sign = Material.SPRUCE_SIGN.createBlockData().createBlockState() as Sign
        private val FAKE_STATE: BlockState = (Material.WATER.createBlockData() as Levelled).also {
            it.level = 7
        }.createBlockState()
    }

    override fun getRealState(): TileState = STATE

    override fun getParticleBase(): ItemStack? = null

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "water_puddle"

    override fun getItem(): CustomItem = Items.WATTER_PUDDLE

    override fun getFakeState(): BlockState = FAKE_STATE

    override fun onDamage(p0: BlockDamageEvent) {}

    override fun doCustomBreakTimeAndAnimation(): Boolean = false

    override fun load(p0: TileState) {}

    override fun unload(p0: TileState) {}
}