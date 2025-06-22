package me.udnek.fnafu.player

import org.bukkit.entity.Player


class PlayerContainer : Iterable<FnafUPlayer>{
    private val players: MutableList<FnafUPlayer> = ArrayList()

    val all: List<FnafUPlayer>
        get() {
            return players
        }

    val aliveSurvivorsAmount: Int
        get() = getSurvivors(false).count { it.status == FnafUPlayer.Status.ALIVE }


    fun getPlayer(player: Player): FnafUPlayer? {
        return players.firstOrNull { it.player == player}
    }

    fun add(player: FnafUPlayer): Boolean {
        if (contains(player.player)) return false
        return players.add(player)
    }

    fun remove(player: Player): Boolean {
        val playerList = players
        for (i in playerList.indices) {
            if (playerList[i].player == player) {
                playerList.removeAt(i)
                return true
            }
        }
        return false
    }

    fun getSurvivors(returnCopy: Boolean): MutableList<FnafUPlayer> {
        val survivors = getPlayersWithType(FnafUPlayer.Type.SURVIVOR)
        if (returnCopy) return ArrayList(survivors)
        return survivors
    }

    fun getAnimatronics(returnCopy: Boolean): MutableList<FnafUPlayer> {
        val animatronics = getPlayersWithType(FnafUPlayer.Type.ANIMATRONIC)
        if (returnCopy) return ArrayList(animatronics)
        return animatronics
    }

    fun getPlayersWithType(type: FnafUPlayer.Type): ArrayList<FnafUPlayer>{
        val playersWithType = ArrayList<FnafUPlayer>()
        for (player in players){
            if (player.type == type){
                playersWithType.add(player)
            }
        }
        return playersWithType
    }

    fun getPlayers(returnCopy: Boolean): MutableList<FnafUPlayer> {
        if (returnCopy) return ArrayList(players)
        return players
    }

    fun contains(player: Player): Boolean = players.any { it.player == player }

    override fun iterator(): Iterator<FnafUPlayer> = players.iterator()
}
