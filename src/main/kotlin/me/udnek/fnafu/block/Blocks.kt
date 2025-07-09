package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.block.decor.ArcadeMachineBlock
import me.udnek.fnafu.block.decor.NightstandBlock
import me.udnek.fnafu.block.decor.VendingMachineBlock

object Blocks {

    val PETROL_STATION = register(PetrolStationBlock())
    val GENERATOR = register(GeneratorBlock())
    val SYSTEM_STATION = register(SystemStationBlock())

    val ARCADE_MACHINE = register(ArcadeMachineBlock())
    val VENDING_MACHINE = register(VendingMachineBlock())
    val NIGHTSTAND = register(NightstandBlock())
    val GREY_STARS_ON_CEIL = register(FnafUBlock("grey_stars_on_ceil"))
    val GOLDEN_STARS_ON_CEIL = register(FnafUBlock("golden_stars_on_ceil"))
    val UNIVERSAL_SLAB = register(UniversalSlabBlock())

    private fun register(customBlockType: CustomBlockType): CustomBlockType {
        return CustomRegistries.BLOCK_TYPE.register(FnafU.instance, customBlockType)
    }
}