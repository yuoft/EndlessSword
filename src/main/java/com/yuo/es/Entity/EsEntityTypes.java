package com.yuo.es.Entity;

import com.yuo.es.EndlessSword;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EsEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, EndlessSword.MOD_ID);

    public static final RegistryObject<EntityType<ColorLightBolt>> COLOR_LIGHT_BOLT = ENTITY_TYPES.register("color_light_bolt",
            () -> EntityType.Builder.<ColorLightBolt>create(ColorLightBolt::new, EntityClassification.MISC).disableSerialization().trackingRange(16).updateInterval(Integer.MAX_VALUE)
                    .size(0.0f, 0.0F).build("color_light_bolt"));

}
