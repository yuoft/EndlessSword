package com.yuo.es.Event;

import com.yuo.endless.EndlessTabs;
import com.yuo.es.EndlessSword;
import com.yuo.es.Items.ESItems;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = EndlessSword.MOD_ID, bus = Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void  addTabs(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey() == EndlessTabs.ENDLESSS_TAB.getKey()) {
            event.accept(ESItems.infinitySb.get());
        }
    }
}
