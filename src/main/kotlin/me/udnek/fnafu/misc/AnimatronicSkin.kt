package me.udnek.fnafu.misc

import me.udnek.fnafu.player.FnafUPlayer
import net.skinsrestorer.api.SkinsRestorerProvider
import net.skinsrestorer.api.property.SkinProperty
import org.bukkit.entity.Player

class AnimatronicSkin {
    companion object {
        val FREDDY = AnimatronicSkin("freddy",
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MTEzMjgzMjU1OCwKICAicHJvZmlsZUlkIiA6ICJhM2E4MTVhM2I2NzE0NGU5OTNmNTU5MjY3NTNkMDQyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVZG5layIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zMzk1ZmM3YjJjOGI0YzU3MzcxMjcxYjhmNWY2NjcxYmE1NjE5MWQ5YjM4ZmExZDk1OTNjYjg1NjE0N2JiOTFlIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZmQ1NTNiMzkzNThhMjRlZGZlM2I4YTlhOTM5ZmE1ZmE0ZmFhNGQ5YTljM2Q2YWY4ZWFmYjM3N2ZhMDVjMmJiIgogICAgfQogIH0KfQ==",
            "kFCXazUV9DZKMkbzGIm/yhXt/4UTbPOF1xUJdWsYrX2adMWHHM/Os+4n4XQBlhiWzi6xg4f8y54Q8sA1hB8Fx2QwEzQHuSYDkUzlCaa+HxXdcddcGjySP5NteNlGtlY1Hbt3U2xOUnl7rxfE8CRA4pxkqdDjkr2fKTf036UOea9/GD71vs/FxcJeHdz84+JefCJRu5iVFtqz9/D0E3T+Yq6uo85ayR9SnANF9DGbCou4Ix9l7DJjG5Box+vkyBtsvb9l+kSrwafkjpCGT0qm/ZqU28e3N2YgJHayO06G3/d79/UE7zLEtFffwkmqWBC8LLD2vDOjUfqz+DBwZPaoWsXz+jNyfRXaC9phv++f3lWs3BL8f+kTG3D2cPDXSjw/2/9M5xfSyc0745GS1LSd7StuiWx5szMe/NwPWblWunr975LA1S+sOdogPePqJs4n577AVOyIJwnE1McTH+Fw1khTZRcyiSlGHfzA5jI4OMfzpN1MveXVTxCl7UjAdUFCRp+GqQoKgWElFCMsCWyEj5L+BfudRdcaeNlE1JNGrn7JvdYL12JELs31LfNbl1fd6GccVnVg65kVzVmOGg2ouA5RzH0trgDTCirdqTocHjcGiWLzdhdS33xOYbV5ohCraNPh854Kn7NN6yL86CvX0p0LprrWnHZnZQwZ8XzdfQI=")
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