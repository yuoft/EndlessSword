package com.yuo.es.Event;

import com.yuo.es.EndlessSword;
import com.yuo.es.Items.ESItems;
import mods.flammpfeil.slashblade.client.ClientHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EndlessSword.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientHandler {

    @SubscribeEvent
    public static void Baked(ModelEvent.ModifyBakingResult event) {
        ClientHandler.bakeBlade(ESItems.infinitySb.get(), event);
//        ClientHandler.bakeBlade(ESItems.warpSb.get(), event);
    }
}
