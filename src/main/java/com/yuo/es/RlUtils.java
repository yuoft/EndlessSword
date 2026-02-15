package com.yuo.es;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("removal")
public class RlUtils {

    public static ResourceLocation fa(String path){
        return new ResourceLocation(EndlessSword.MOD_ID, path);
    }

    public static ResourceLocation wdn(String path){
        return new ResourceLocation(path);
    }

    public static ResourceLocation tryParse(String path){
        try {
            return new ResourceLocation(path);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
