package me.udnek.fnafu.item

import me.udnek.coreu.custom.component.instance.BlockPlacingItem
import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.item.ConstructableCustomItem
import org.bukkit.Material

open class FnafUBlockItem(val rawID: String, val block: CustomBlockType) : ConstructableCustomItem(){

    override fun getMaterial(): Material = DEFAULT_MATERIAL_FOR_BLOCK_PLACEABLE

    override fun getRawId(): String = rawID

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(BlockPlacingItem(block))
    }

}
