package me.udnek.fnafu.misc

import me.udnek.fnafu.player.FnafUPlayer
import net.skinsrestorer.api.SkinsRestorerProvider
import net.skinsrestorer.api.property.SkinProperty
import org.bukkit.entity.Player

class AnimatronicSkin {
    companion object {
        val FREDDY = AnimatronicSkin("freddy",
            "ewogICJ0aW1lc3RhbXAiIDogMTY4OTkwNTQ5NzI5OCwKICAicHJvZmlsZUlkIiA6ICIyMDZlMWZkYjI5Yzk0NGYxOTQ5OTg4NzAwNTQxMGQ2NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJoNHlsMzMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM5NWZjN2IyYzhiNGM1NzM3MTI3MWI4ZjVmNjY3MWJhNTYxOTFkOWIzOGZhMWQ5NTkzY2I4NTYxNDdiYjkxZSIKICAgIH0KICB9Cn0=",
            "wpiSFuc2tv0fTONu5gfNr8wM6kIzJAjb6/opEgsfKzC1x2EJP5zePUJZY8Jat2G6xXEUn9rQt1iGT1HOSUdKmNeoDVZKO1/X36Pc/Wpq27mUv51USu9Cs7C+vyU2MK/e/lD9cQiYkVpVYNSI5qwHFNfCXNToyliXcYkRxZQ1kMhVIqraCFIHkYS8gs8fRu+JpSLHs7VQG+84662EBMFF5NyMu6LWXxcvQB9hLsiaaLvPIx6x471OKrmGPbvcMxTF2Le+Z0lBKpAk778QlCBqIVj4Y633IWtJ9hAM2gKFsZN6jOsOkr5D5SPe+wklsUuk0y92ccMSp9MydfxunwdBPLvjD1/YrPF5i1YdxmcDFKloGRJD+r25k6M/2tPwOK8ETo6vGPfGCz2/u+3+19yEeBGWHALf6Ehp3A++j+X2/gPL4CXb03lKlbeE7e1sNTd8ME+TH0PWBZDJvImauY1zC0wnp+xfGYdljl3RIKpm0/VQI5MUTyjt5BFXZu0midyEPzPy6FgCm9XCXRcX8gbhawCQWDpPHSAWEEtQ9LxtTNVVjRX30HxEvIWvpNPhxXRt1bckONnqdKHUQ6OU08IxeG7UZ5K4Y1dURZmCjkwbemj5yGHDccpUtmUXQeulJnP6TgpIeeF14u+sTqsViZ9uZk7jyf6D980WyyB/8TiKr9M=")
        val SPRINGTRAP = AnimatronicSkin("springtrap",
            "ewogICJ0aW1lc3RhbXAiIDogMTY3NDkxOTQ2NTU4MCwKICAicHJvZmlsZUlkIiA6ICJhM2E4MTVhM2I2NzE0NGU5OTNmNTU5MjY3NTNkMDQyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVZG5layIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81ZWI5M2FhYWY3MDFjODY4YTUyNWE5ZDNkZDk4ZGFlZDkzNzRkMmExMTgwMDNiZDZmMzQ2NDc3OGMzNjFlMWU4IgogICAgfQogIH0KfQ==",
            "vmKnVp+FwK3fVlKPFqg6wnQ3iLioXdnBKBldxfAKZljAx5vTTEemCa4JJUwf+CZ/7IygQHjjJOgt/EoOf+7pIqJzlUyTv127hrVTOY+3M7fB4HCvTJQW28IzuTJnIJOkSBrHv27JTWW7IS0VxHz1JM+Ed470K3BpPWfMAjdNch6Cg2VVYivZ2QWkFylBhMjeq4Q2j30/Dk73zDu1AtVC51ijaIeXxoLECLBGvtPGFQkKDZXshWY5wBGPWuLvXTYUL1xiv4qpWnR1GUS0vz1IYv+5a6ev6uEgfPxgZfalLaNTIIEbedQCGwZx4n2LOqyXQAgaGmPkXsWn2z8Y5CGn+22Mm7yaz4XZDiQ5v1g1B2uvmF/h5NTN50luLfFf5axjaE93tPhYo/7tN8/Syi8OGIzcN3XMEigZ4+w7jov+Ich8eiJnhvGFV5ZYichLPt+r2w6/EyV86c2cKRL6Hryn4GfXWGh6P/JRKi3o223G/WQVd3DigcEdLKQVMMMbqLz+4iakn4ubv2EKowBcuWYaSPH5vQxlSuVDIb677obWAP5boNSBBrxBWqJ5BB90BNDJ+Gq2aLCAKmDqvnPJeSbG5PZk1yiWbImpedup+rJ9Be/PYWdxNGq7xY3SPR838MWB2c3ZRXBp+Ccwkqzq1lPq0QwlXYVK+dsGAXtoUGPnXP8=")
    }

    private val id: String

    constructor(id: String, value: String, signature: String) {
        this.id = id
        SkinsRestorerProvider.get().skinStorage.setCustomSkinData(id, SkinProperty.of(value, signature))
    }

    fun setSkin(player: FnafUPlayer) {
        val skinsRestorerAPI = SkinsRestorerProvider.get()
        skinsRestorerAPI.playerStorage.setSkinIdOfPlayer(player.player.uniqueId, skinsRestorerAPI.skinStorage.findSkinData(id).get().identifier)
        skinsRestorerAPI.getSkinApplier(Player::class.java).applySkin(player.player)
    }
}