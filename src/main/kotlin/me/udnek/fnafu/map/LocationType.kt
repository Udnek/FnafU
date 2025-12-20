package me.udnek.fnafu.map

enum class LocationType {
    SPAWN_ANIMATRONIC,
    SPAWN_SURVIVOR,
    PRESPAWN_ANIMATRONIC,
    RESPAWN_SURVIVOR,
    @Deprecated("not used")
    PICK_STAGE_SPAWN_ANIMATRONIC,
    @Deprecated("not used")
    PICK_STAGE_SPAWN_SURVIVOR
}
