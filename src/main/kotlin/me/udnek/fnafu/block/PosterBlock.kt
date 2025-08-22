package me.udnek.fnafu.block

import io.papermc.paper.event.player.PlayerPickBlockEvent
import me.udnek.coreu.custom.component.instance.MiddleClickableBlock
import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.entitylike.block.constructabletype.AbstractCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.item.decor.Poster
import me.udnek.fnafu.misc.getCustom
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.BlockState
import org.bukkit.block.Sign
import org.bukkit.block.TileState
import org.bukkit.block.data.type.WallSign
import org.bukkit.block.sign.Side
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable

class PosterBlock : AbstractCustomBlockType {

    companion object {
        val TILE_STATE = Material.OAK_WALL_SIGN.createBlockData().createBlockState() as Sign
        val ITEM_PDS = NamespacedKey(FnafU.instance, "item_id")
    }

    constructor(){
        components.set(object : MiddleClickableBlock.Implementation() {
            override fun getItemForCreative(block: CustomBlockType, event: PlayerPickBlockEvent): ItemStack {
                val id = (event.block.state as TileState).persistentDataContainer.get(ITEM_PDS, PersistentDataType.STRING) ?: return super.getItemForCreative(block, event)
                return CustomItem.get(id)?.item ?: super.getItemForCreative(block, event)
            }
        })
    }

    override fun load(state: TileState) {
        updateText(state as Sign)
    }

    override fun place(location: Location, context: CustomBlockPlaceContext) {
        super.place(location, context)
        val event = context.event ?: return
        val poster = event.itemInHand.getCustom() as? Poster ?: return
        val block = event.block
        val face = event.blockAgainst.getFace(block)!!
        block.setBlockData((block.blockData as WallSign).also {
            it.facing = face
        }, false)
        val sign = block.state as Sign
        sign.persistentDataContainer.set(ITEM_PDS, PersistentDataType.STRING, poster.id)
        sign.isWaxed = true
        sign.update()
        object : BukkitRunnable(){
            override fun run() = updateText(location.block.state as Sign)
        }.runTaskLater(FnafU.instance, 3)
    }

    fun updateText(sign: Sign){
        val itemId = sign.persistentDataContainer.get(ITEM_PDS, PersistentDataType.STRING)
        val poster = CustomItem.get(itemId) as? Poster ?: return
        val side = sign.getSide(Side.FRONT)
        side.isGlowingText = poster.isGlowing
        side.color = poster.color
        side.line(0, Component.text(poster.fontChar).font(Poster.FONT))
        sign.update()
    }

    override fun getRealState(): TileState = TILE_STATE

    override fun getParticleBase(): ItemStack? = null

    override fun getLoot(): Either<LootTable, List<ItemStack>>? = null

    override fun getBreakSpeedBaseBlock(): Material = Material.OAK_PLANKS

    override fun getRawId(): String = "poster"

    override fun getItem(): CustomItem = Items.POSTER.RULES

    override fun getFakeState(): BlockState? = null

    override fun onDamage(p0: BlockDamageEvent){}

    override fun doCustomBreakTimeAndAnimation(): Boolean = false

    override fun unload(p0: TileState) {}
}