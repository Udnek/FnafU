package me.udnek.fnafu.item;

import me.udnek.fnafu.FnafU;
import me.udnek.fnafu.mechanic.camera.Camera;
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

    public ItemStack getWithCamera(Camera camera){
        ItemStack item = getItem();
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, camera.getId());
        itemMeta.displayName(Component.text(camera.getId()));
        item.setItemMeta(itemMeta);
        return item;
    }

    public String getCameraId(ItemStack itemStack){
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        String camera = persistentDataContainer.get(namespacedKey, PersistentDataType.STRING);
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
