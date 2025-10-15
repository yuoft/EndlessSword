package com.yuo.es;

import com.yuo.es.Items.ESItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

//创造模式物品栏 实例化
public class ESTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EndlessSword.MOD_ID);
	public static final RegistryObject<CreativeModeTab> ES_TAB = TABS.register(EndlessSword.MOD_ID + "_tab", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.tab.ES"))
			.icon(() -> ESItems.infinitySb.get().getDefaultInstance())
			.displayItems((parameters, output) -> {
				for (RegistryObject<Item> entry : ESItems.ITEMS.getEntries()) {
						output.accept(entry.get());
				}


			}).build());
}
