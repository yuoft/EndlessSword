package com.yuo.es.Items;

import com.yuo.es.EndlessSword;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class ESItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndlessSword.MOD_ID);

	public static RegistryObject<Item> infinitySb = ITEMS.register("infinity_sb", InfinitySB::new);
	public static RegistryObject<Item> warpSb = ITEMS.register("warp_sb", WarpSB::new);
}
