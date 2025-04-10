package me.udnek.fnafu.player

import org.bukkit.entity.Player


class PlayerContainer {
    private val survivors: MutableList<FnafUPlayer> = ArrayList()
    private val animatronics: MutableList<FnafUPlayer> = ArrayList()

    val all: List<FnafUPlayer>
        get() {
            val fnafUPlayers = ArrayList<FnafUPlayer>()
            fnafUPlayers.addAll(survivors)
            fnafUPlayers.addAll(animatronics)
            return fnafUPlayers
        }

    val aliveSurvivorsAmount: Int
        get() {
            return survivors.size
        }


    fun getSurvivor(player: Player): FnafUPlayer? {
        return survivors.firstOrNull { it.player == player }
    }

    fun getAnimatronic(player: Player): FnafUPlayer? {
        return animatronics.firstOrNull { it.player == player }
    }


    fun getPlayer(player: Player): FnafUPlayer? {
        val survivor = getSurvivor(player)
        if (survivor != null) return survivor
        return getAnimatronic(player)
    }

    fun addSurvivor(survivor: FnafUPlayer): Boolean {
        if (containsSurvivor(survivor.player)) return false
        return survivors.add(survivor)
    }

    fun addAnimatronic(animatronic: FnafUPlayer): Boolean {
        if (containsAnimatronic(animatronic.player)) return false
        return animatronics.add(animatronic)
    }

    fun remove(player: Player): Boolean {
        val survivorList = survivors
        for (i in survivorList.indices) {
            val survivor = survivorList[i]
            if (survivor.player == player) {
                survivorList.removeAt(i)
                return true
            }
        }
        val animatronicList = animatronics
        for (i in animatronicList.indices) {
            val animatronic = animatronicList[i]
            if (animatronic.player == player) {
                animatronicList.removeAt(i)
                return true
            }
        }
        return false
    }

    fun getSurvivors(returnCopy: Boolean): MutableList<FnafUPlayer> {
        if (returnCopy) return ArrayList(survivors)
        return survivors
    }

    fun getAnimatronics(returnCopy: Boolean): MutableList<FnafUPlayer> {
        if (returnCopy) return ArrayList(animatronics)
        return animatronics
    }


    fun contains(player: Player): Boolean = containsSurvivor(player) || containsAnimatronic(player)

    fun containsSurvivor(player: Player): Boolean = survivors.any { it.player == player }

    fun containsAnimatronic(player: Player): Boolean = animatronics.any { it.player == player }
}
