package com.yuo.es.Entity;

import com.yuo.es.EndlessSword;
import com.yuo.es.Items.InfinitySbItemEntity;
import mods.flammpfeil.slashblade.entity.BladeItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EsEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EndlessSword.MOD_ID);

    public static final RegistryObject<EntityType<InfinitySbItemEntity>> INFINITY_SB = ENTITY_TYPES.register("infinity_sb",
            () -> EntityType.Builder.<InfinitySbItemEntity>of(InfinitySbItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(20).setCustomClientFactory(InfinitySbItemEntity::createInstanceFromPacket).build("infinity_sb"));
}
