package me.udnek.fnafu.component.survivor


import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.entity.Entity

open class SpectateEntityData : MGUPlayerData {

    companion object {
        val DEFAULT = object : SpectateEntityData() {
             override fun spectate(player: FnafUPlayer, target: Entity) = throwCanNotChangeDefault()
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

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder, out MGUPlayerData> {
        return FnafUComponents.SPECTATE_ENTITY_DATA
    }

    override fun reset() {
        spectatingEntity = null
    }
}
