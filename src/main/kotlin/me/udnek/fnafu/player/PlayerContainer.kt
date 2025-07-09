package me.udnek.fnafu.player

import org.bukkit.entity.Player


class PlayerContainer : Iterable<FnafUPlayer>{
    private val players: MutableList<FnafUPlayer> = ArrayList()

    val all: List<FnafUPlayer>
        get() = players

    val aliveSurvivorsAmount: Int
        get() = aliveSurvivors.size

    val aliveSurvivors: List<FnafUPlayer>
        get() = survivors.filter{ it.status == FnafUPlayer.Status.ALIVE }

    val survivors: List<FnafUPlayer>
        get() = getPlayersWithType(FnafUPlayer.Type.SURVIVOR)

    val animatronics: List<FnafUPlayer>
        get() = getPlayersWithType(FnafUPlayer.Type.ANIMATRONIC)


    fun getPlayer(player: Player): FnafUPlayer? {
        return players.firstOrNull { it.player == player}
    }

    fun add(player: FnafUPlayer): Boolean {
        if (contains(player.player)) return false
        return players.add(player)
    }

    fun remove(player: Player): Boolean {
        return players.removeIf { it.player == player }
    }


    fun getPlayersWithType(type: FnafUPlayer.Type): List<FnafUPlayer>{
        return players.filter { it.type == type }
    }

    fun contains(player: Player): Boolean = players.any { it.player == player }

    override fun iterator(): Iterator<FnafUPlayer> = players.iterator()
}
