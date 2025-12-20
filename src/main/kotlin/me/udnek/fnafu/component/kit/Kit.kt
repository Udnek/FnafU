package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.component.ComponentHolder
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.component.instance.TranslatableThing
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
import me.udnek.fnafu.item.Items.FLASHLIGHT
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
import net.kyori.adventure.translation.Translatable
import org.bukkit.inventory.ItemStack

interface Kit : MGUPlayerData, Registrable, ComponentHolder<Kit>, Translatable{

    companion object {
        val REGISTRY: CustomRegistry<Kit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(AnimatronicKit(
            "springtrap",
            FnafUPlayer.Type.ANIMATRONIC,
            TranslatableThing.ofEngAndRu("Springtrap", "Спрингтрап"),
            SPRINGTRAP_PLUSHTRAP_ABILITY,
            listOf(SPRINGTRAP_PLUSHTRAP_ABILITY, SPRINGTRAP_BREAK_CAMERAS, SPRINGTRAP_MASK),
            Sounds.SPRINGTRAP_JUMP_SCARE, AnimatronicSkin.SPRINGTRAP))
        val FREDDY = register(AnimatronicKit(
            "freddy",
            FnafUPlayer.Type.ANIMATRONIC,
            TranslatableThing.ofEngAndRu("Freddy", "Фредди"),
            FREDDY_SHADOW,
            listOf(FREDDY_SHADOW, FREDDY_SET_TRAP, FREDDY_TELEPORT_TO_TRAP, FREDDY_MASK),
            Sounds.FREDDY_JUMP_SCARE, AnimatronicSkin.FREDDY))

        val CAMERAMAN = register(ConstructableKit(
            "cameraman",
            FnafUPlayer.Type.SURVIVOR,
            TranslatableThing.ofEng("Cameraman"),
            FULL_CAMERA_TABLET,
            listOf(FULL_CAMERA_TABLET, SMALL_PETROL_CANISTER, FLASHLIGHT)))
        val SYSTEMMAN = register(ConstructableKit(
            "systemman",
            FnafUPlayer.Type.SURVIVOR,
            TranslatableThing.ofEng("Sysadmin"),
            SYSTEM_TABLET,
            listOf(SYSTEM_TABLET, CUT_CAMERA_TABLET, SMALL_PETROL_CANISTER, FLASHLIGHT)
        ))
        val REFUELLER = register(ConstructableKit(
            "refueller",
            FnafUPlayer.Type.SURVIVOR,
            TranslatableThing.ofEng("Refueller"),
            LARGE_PETROL_CANISTER,
            listOf(CUT_CAMERA_TABLET, LARGE_PETROL_CANISTER, FLASHLIGHT)
        ))
        val DOORMAN = register(ConstructableKit(
            "doorman",
            FnafUPlayer.Type.SURVIVOR,
            TranslatableThing.ofEng("Doorman"),
            DOOR_TABLET,
            listOf(DOOR_TABLET, CUT_CAMERA_TABLET, SMALL_PETROL_CANISTER, FLASHLIGHT)))
        val VENTILATIONMAN = register(ConstructableKit(
            "ventilationman",
            FnafUPlayer.Type.SURVIVOR,
            TranslatableThing.ofEng("HVAC Technician"),
            VENTILATION_TABLET,
            listOf(VENTILATION_TABLET, CUT_CAMERA_TABLET, SMALL_PETROL_CANISTER, FLASHLIGHT)))

        fun register(kit: Kit): Kit {
            return REGISTRY.register(FnafU.instance, kit)
        }
    }

    val displayItem: ItemStack
    val items: List<ItemStack>
    val playerType: FnafUPlayer.Type
    var jumpScareSound: CustomSound?

    fun setUp(player: FnafUPlayer)

    fun giveToCurrentInventory(player: FnafUPlayer)

    override fun translationKey(): String {
        return "kit.${key.namespace}.${key.value()}"
    }

    override fun getType(): CustomComponentType<in MGUPlayerDataHolder, out CustomComponent<in MGUPlayerDataHolder>?> {
        return FnafUComponents.KIT
    }

    override fun reset() {}
}
