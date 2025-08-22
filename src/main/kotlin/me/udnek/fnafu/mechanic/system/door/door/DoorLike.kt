package me.udnek.fnafu.mechanic.system.door.door

import me.udnek.coreu.mgu.Originable
import me.udnek.coreu.mgu.Resettable
import org.bukkit.Location

interface DoorLike : Originable, Resettable{

    val isClosed: Boolean
    var isLocked: Boolean
    val tabletMenuPosition: Int
    val stunCenter: Location
    val debugLocation: Location

    fun open()
    fun close()
    fun toggle(){
        if (isClosed) open()
        else close()
    }
}
