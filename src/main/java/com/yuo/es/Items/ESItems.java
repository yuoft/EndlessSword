package com.yuo.es.Items;

import com.yuo.es.EndlessSword;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

//物品注册管理器
public class ESItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndlessSword.MOD_ID);

	public static RegistryObject<Item> infinitySb = ITEMS.register("infinity_sb", InfinitySB::new);
//	public static RegistryObject<Item> warpSb = ITEMS.register("warp_sb", WarpSB::new);
}
