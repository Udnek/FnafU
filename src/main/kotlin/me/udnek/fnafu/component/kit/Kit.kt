package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.registry.CustomRegistry
import me.udnek.coreu.custom.registry.MappedCustomRegistry
import me.udnek.coreu.custom.registry.Registrable
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items.CUT_CAMERA_TABLET
import me.udnek.fnafu.item.Items.DOOR_TABLET
import me.udnek.fnafu.item.Items.FREDDY_MASK
import me.udnek.fnafu.item.Items.FREDDY_SET_TRAP
import me.udnek.fnafu.item.Items.FREDDY_SHADOW
import me.udnek.fnafu.item.Items.FREDDY_TELEPORT_TO_TRAP
import me.udnek.fnafu.item.Items.FULL_CAMERA_TABLET
import me.udnek.fnafu.item.Items.LARGE_PETROL_CANISTER
import me.udnek.fnafu.item.Items.SMALL_PETROL_CANISTER
import me.udnek.fnafu.item.Items.SPRINGTRAP_BREAK_CAMERAS
import me.udnek.fnafu.item.Items.SPRINGTRAP_MASK
import me.udnek.fnafu.item.Items.SPRINGTRAP_PLUSHTRAP_ABILITY
import me.udnek.fnafu.item.Items.SYSTEM_TABLET
import me.udnek.fnafu.item.Items.VENTILATION_TABLET
import me.udnek.fnafu.misc.AnimatronicSkin
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import org.bukkit.inventory.ItemStack

interface Kit : MGUPlayerData, Registrable{

    companion object {
        val REGISTRY: CustomRegistry<Kit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(AnimatronicKit("springtrap", FnafUPlayer.Type.ANIMATRONIC, SPRINGTRAP_PLUSHTRAP_ABILITY,
            listOf(SPRINGTRAP_PLUSHTRAP_ABILITY, SPRINGTRAP_BREAK_CAMERAS, SPRINGTRAP_MASK), Sounds.SPRINGTRAP_JUMP_SCARE, AnimatronicSkin.SPRINGTRAP))
        val FREDDY = register(AnimatronicKit("freddy", FnafUPlayer.Type.ANIMATRONIC, FREDDY_SHADOW, listOf(FREDDY_SHADOW, FREDDY_SET_TRAP,
            FREDDY_TELEPORT_TO_TRAP, FREDDY_MASK), Sounds.FREDDY_JUMP_SCARE, AnimatronicSkin.FREDDY))

        val CAMERAMAN = register(ConstructableKit("cameraman", FnafUPlayer.Type.SURVIVOR, FULL_CAMERA_TABLET, listOf(FULL_CAMERA_TABLET), listOf(SMALL_PETROL_CANISTER)))
        val DOORMAN = register(ConstructableKit("doorman", FnafUPlayer.Type.SURVIVOR, DOOR_TABLET, listOf(DOOR_TABLET, CUT_CAMERA_TABLET), listOf(SMALL_PETROL_CANISTER)))
        val VENTILATIONMAN = register(ConstructableKit("ventilationman", FnafUPlayer.Type.SURVIVOR, VENTILATION_TABLET, listOf(VENTILATION_TABLET, CUT_CAMERA_TABLET), listOf(SMALL_PETROL_CANISTER)))
        val SYSTEMMAN = register(ConstructableKit("systemman", FnafUPlayer.Type.SURVIVOR, SYSTEM_TABLET, listOf(SYSTEM_TABLET, CUT_CAMERA_TABLET), listOf(SMALL_PETROL_CANISTER)))
        val REFUELLER = register(ConstructableKit("refueller", FnafUPlayer.Type.SURVIVOR, LARGE_PETROL_CANISTER, listOf(CUT_CAMERA_TABLET), listOf(LARGE_PETROL_CANISTER)))

        fun register(kit: Kit): Kit {
            return REGISTRY.register(FnafU.instance, kit)
        }
    }

    val displayItem: ItemStack
    val permanentItems: List<ItemStack>
    val inventoryItems: List<ItemStack>
    val playerType: FnafUPlayer.Type
    var jumpScareSound: CustomSound?

    fun setUp(player: FnafUPlayer)

    fun regive(player: FnafUPlayer)

    fun regiveCurrentInventory(player: FnafUPlayer)

    override fun getType(): CustomComponentType<in MGUPlayerDataHolder, out CustomComponent<in MGUPlayerDataHolder>?> {
        return FnafUComponents.KIT
    }

    override fun reset() {}
}
