package me.udnek.fnafu.item;

import me.udnek.fnafu.ability.Abilities;
import me.udnek.itemscoreu.customitem.CustomModelDataItem;
import org.bukkit.Material;

public class CameraTablet extends CustomModelDataItem implements AbilityItem{
    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_SWORD;
    }

    @Override
    protected String getRawDisplayName() {return "item.fnafu.camera_tablet";}

    @Override
    protected String getItemName() {
        return "camera_tablet";
    }

    @Override
    public Abilities getActivatingAbility() {
        return Abilities.CAMERA_TABLET;
    }
}
