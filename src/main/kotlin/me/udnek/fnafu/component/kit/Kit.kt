package me.udnek.fnafu.component.kit

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
import me.udnek.fnafu.item.Items.LARGE_PETROL_CANISTER
import me.udnek.fnafu.item.Items.CUT_CAMERA_TABLET
import me.udnek.fnafu.item.Items.DOOR_TABLET
import me.udnek.fnafu.item.Items.FREDDY_MASK
import me.udnek.fnafu.item.Items.FREDDY_SET_TRAP
import me.udnek.fnafu.item.Items.FREDDY_SHADOW
import me.udnek.fnafu.item.Items.FREDDY_TELEPORT_TO_TRAP
import me.udnek.fnafu.item.Items.FULL_CAMERA_TABLET
import me.udnek.fnafu.item.Items.SMALL_PETROL_CANISTER
import me.udnek.fnafu.item.Items.SPRINGTRAP_BREAK_CAMERAS
import me.udnek.fnafu.item.Items.SPRINGTRAP_MASK
import me.udnek.fnafu.item.Items.SPRINGTRAP_PLUSHTRAP_ABILITY
import me.udnek.fnafu.item.Items.SYSTEM_TABLET
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import org.bukkit.inventory.ItemStack

interface Kit : MGUPlayerData, Registrable{

    companion object {
        val REGISTRY: CustomRegistry<Kit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(AnimatronicKit("springtrap", FnafUPlayer.Type.ANIMATRONIC, SPRINGTRAP_PLUSHTRAP_ABILITY,
            listOf(SPRINGTRAP_PLUSHTRAP_ABILITY, SPRINGTRAP_BREAK_CAMERAS, SPRINGTRAP_MASK), Sounds.SPRINGTRAP_JUMP_SCARE,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MTEzMjgzMjU1OCwKICAicHJvZmlsZUlkIiA6ICJhM2E4MTVhM2I2NzE0NGU5OTNmNTU5MjY3NTNkMDQyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVZG5layIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zMzk1ZmM3YjJjOGI0YzU3MzcxMjcxYjhmNWY2NjcxYmE1NjE5MWQ5YjM4ZmExZDk1OTNjYjg1NjE0N2JiOTFlIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZmQ1NTNiMzkzNThhMjRlZGZlM2I4YTlhOTM5ZmE1ZmE0ZmFhNGQ5YTljM2Q2YWY4ZWFmYjM3N2ZhMDVjMmJiIgogICAgfQogIH0KfQ==", "kFCXazUV9DZKMkbzGIm/yhXt/4UTbPOF1xUJdWsYrX2adMWHHM/Os+4n4XQBlhiWzi6xg4f8y54Q8sA1hB8Fx2QwEzQHuSYDkUzlCaa+HxXdcddcGjySP5NteNlGtlY1Hbt3U2xOUnl7rxfE8CRA4pxkqdDjkr2fKTf036UOea9/GD71vs/FxcJeHdz84+JefCJRu5iVFtqz9/D0E3T+Yq6uo85ayR9SnANF9DGbCou4Ix9l7DJjG5Box+vkyBtsvb9l+kSrwafkjpCGT0qm/ZqU28e3N2YgJHayO06G3/d79/UE7zLEtFffwkmqWBC8LLD2vDOjUfqz+DBwZPaoWsXz+jNyfRXaC9phv++f3lWs3BL8f+kTG3D2cPDXSjw/2/9M5xfSyc0745GS1LSd7StuiWx5szMe/NwPWblWunr975LA1S+sOdogPePqJs4n577AVOyIJwnE1McTH+Fw1khTZRcyiSlGHfzA5jI4OMfzpN1MveXVTxCl7UjAdUFCRp+GqQoKgWElFCMsCWyEj5L+BfudRdcaeNlE1JNGrn7JvdYL12JELs31LfNbl1fd6GccVnVg65kVzVmOGg2ouA5RzH0trgDTCirdqTocHjcGiWLzdhdS33xOYbV5ohCraNPh854Kn7NN6yL86CvX0p0LprrWnHZnZQwZ8XzdfQI="))
        val FREDDY = register(AnimatronicKit("freddy", FnafUPlayer.Type.ANIMATRONIC, FREDDY_SHADOW, listOf(FREDDY_SHADOW, FREDDY_SET_TRAP,
            FREDDY_TELEPORT_TO_TRAP, FREDDY_MASK), Sounds.SPRINGTRAP_JUMP_SCARE, "ewogICJ0aW1lc3RhbXAiIDogMTc1MTEzMjgzMjU1OCwKICAicHJvZmlsZUlkIiA6ICJhM2E4MTVhM2I2NzE0NGU5OTNmNTU5MjY3NTNkMDQyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVZG5layIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zMzk1ZmM3YjJjOGI0YzU3MzcxMjcxYjhmNWY2NjcxYmE1NjE5MWQ5YjM4ZmExZDk1OTNjYjg1NjE0N2JiOTFlIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZmQ1NTNiMzkzNThhMjRlZGZlM2I4YTlhOTM5ZmE1ZmE0ZmFhNGQ5YTljM2Q2YWY4ZWFmYjM3N2ZhMDVjMmJiIgogICAgfQogIH0KfQ==", "kFCXazUV9DZKMkbzGIm/yhXt/4UTbPOF1xUJdWsYrX2adMWHHM/Os+4n4XQBlhiWzi6xg4f8y54Q8sA1hB8Fx2QwEzQHuSYDkUzlCaa+HxXdcddcGjySP5NteNlGtlY1Hbt3U2xOUnl7rxfE8CRA4pxkqdDjkr2fKTf036UOea9/GD71vs/FxcJeHdz84+JefCJRu5iVFtqz9/D0E3T+Yq6uo85ayR9SnANF9DGbCou4Ix9l7DJjG5Box+vkyBtsvb9l+kSrwafkjpCGT0qm/ZqU28e3N2YgJHayO06G3/d79/UE7zLEtFffwkmqWBC8LLD2vDOjUfqz+DBwZPaoWsXz+jNyfRXaC9phv++f3lWs3BL8f+kTG3D2cPDXSjw/2/9M5xfSyc0745GS1LSd7StuiWx5szMe/NwPWblWunr975LA1S+sOdogPePqJs4n577AVOyIJwnE1McTH+Fw1khTZRcyiSlGHfzA5jI4OMfzpN1MveXVTxCl7UjAdUFCRp+GqQoKgWElFCMsCWyEj5L+BfudRdcaeNlE1JNGrn7JvdYL12JELs31LfNbl1fd6GccVnVg65kVzVmOGg2ouA5RzH0trgDTCirdqTocHjcGiWLzdhdS33xOYbV5ohCraNPh854Kn7NN6yL86CvX0p0LprrWnHZnZQwZ8XzdfQI="))
        val CAMERAMAN = register(ConstructableKit("cameraman", FnafUPlayer.Type.SURVIVOR, FULL_CAMERA_TABLET, listOf(FULL_CAMERA_TABLET), listOf(SMALL_PETROL_CANISTER)))
        val DOORMAN = register(ConstructableKit("doorman", FnafUPlayer.Type.SURVIVOR, DOOR_TABLET, listOf(DOOR_TABLET, CUT_CAMERA_TABLET), listOf(SMALL_PETROL_CANISTER)))
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

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder?, out MGUPlayerData> {
        return FnafUComponents.KIT
    }

    override fun reset() {}
}
