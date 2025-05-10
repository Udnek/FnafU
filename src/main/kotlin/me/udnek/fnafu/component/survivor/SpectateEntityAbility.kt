package me.udnek.fnafu.component.survivor


import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityHolderComponent
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityInstance
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import org.bukkit.entity.Entity

open class SpectateEntityAbility : MGUAbilityInstance {

    companion object {
        val DEFAULT = object : SpectateEntityAbility() {
             override fun spectate(player: FnafUPlayer, target: Entity){
                 throw RuntimeException("Component is default!")
             }
        }
    }

    var spectatingEntity: Entity? = null
        protected set

    open fun spectate(player: FnafUPlayer, target: Entity){
        val protocolManager = ProtocolLibrary.getProtocolManager()
        val packet = protocolManager.createPacket(PacketType.Play.Server.CAMERA)
        packet.integers.write(0, target.entityId)
        protocolManager.sendServerPacket(player.player, packet)
        spectatingEntity = if (player.player == target) null else target
    }

    fun spectateSelf(player: FnafUPlayer){
        spectate(player, player.player)
    }

    override fun getType(): CustomComponentType<out MGUAbilityHolderComponent, out CustomComponent<MGUAbilityHolderComponent>> {
        return Abilities.SPECTATE_ENTITY
    }
}
