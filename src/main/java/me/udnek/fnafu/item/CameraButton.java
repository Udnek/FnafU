package me.udnek.fnafu.item;

import me.udnek.fnafu.FnafU;
import me.udnek.itemscoreu.customitem.CustomModelDataItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CameraButton extends CustomModelDataItem {

    public final NamespacedKey namespacedKey = new NamespacedKey(FnafU.getInstance(), "camera");

    public ItemStack getWithCamera(int camera){
        ItemStack item = getItem();
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(namespacedKey, PersistentDataType.INTEGER, camera);
        itemMeta.displayName(Component.text(camera));
        item.setItemMeta(itemMeta);
        return item;
    }

    public int getCamera(ItemStack itemStack){
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        Integer camera = persistentDataContainer.get(namespacedKey, PersistentDataType.INTEGER);
        if (camera == null) return -1;
        return camera;
    }

    @Override
    public int getCustomModelData() {return 0;}
    @Override
    public Material getMaterial() {return Material.GLASS;}
    @Override
    protected String getRawDisplayName() {return "camera_button";}
    @Override
    protected String getItemName() {return "camera_button";}
}
