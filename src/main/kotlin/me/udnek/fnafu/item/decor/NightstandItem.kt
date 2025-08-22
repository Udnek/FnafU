package me.udnek.fnafu.item.decor

import io.papermc.paper.datacomponent.item.DyedItemColor
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.nms.Nms
import me.udnek.fnafu.block.Blocks
import org.bukkit.Material

class NightstandItem : FnafUBlockDecorItem("nightstand", Blocks.NIGHTSTAND) {

    override fun getDyedColor(): CustomItemProperties.DataSupplier<DyedItemColor> {
        return CustomItemProperties.DataSupplier.of(DyedItemColor.dyedItemColor(Nms.get().getColorByDye(Material.BLUE_DYE)!!.fireworkColor()))
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(AutoGeneratingFilesItem.DYE_COLORABLE)
    }
}