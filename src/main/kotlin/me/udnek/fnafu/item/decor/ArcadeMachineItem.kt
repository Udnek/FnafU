package me.udnek.fnafu.item.decor

import me.udnek.coreu.custom.component.instance.BlockPlacingItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.fnafu.block.Blocks
import org.bukkit.Material

class ArcadeMachineItem : ConstructableCustomItem() {
    override fun getRawId(): String = "arcade_machine"

    override fun getMaterial(): Material = DEFAULT_MATERIAL_FOR_BLOCK_PLACEABLE

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(BlockPlacingItem(Blocks.ARCADE_MACHINE))
    }
}