package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU

object Blocks {

    val ARCADE_MACHINE = register(ArcadeMachineBlock())
    val NIGHTSTAND = register(NightstandBlock())

    private fun register(customBlockType: CustomBlockType): CustomBlockType {
        return CustomRegistries.BLOCK_TYPE.register(FnafU.instance, customBlockType)
    }
}