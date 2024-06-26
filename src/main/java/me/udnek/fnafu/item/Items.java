package me.udnek.fnafu.item;

import me.udnek.fnafu.FnafU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemManager;

public class Items {
    public static final CameraButton cameraButton = (CameraButton) register(new CameraButton());
    public static final CameraTablet cameraTablet = (CameraTablet) register(new CameraTablet());

    private static CustomItem register(CustomItem customItem){
        return CustomItemManager.registerItem(FnafU.getInstance(), customItem);
    }
}
