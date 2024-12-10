package com.yuo.es.Event;

import com.yuo.es.EndlessSword;
import com.yuo.es.Items.ESItems;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EndlessSword.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientHandler {

    @SubscribeEvent
    public static void Baked(ModelBakeEvent event) {
        ModelResourceLocation loc = new ModelResourceLocation(ESItems.infinitySb.getId(), "inventory");
        ModelResourceLocation loc0 = new ModelResourceLocation(ESItems.warpSb.getId(), "inventory");
        BladeModel model = new BladeModel(event.getModelRegistry().get(loc), event.getModelLoader());
        BladeModel model0 = new BladeModel(event.getModelRegistry().get(loc0), event.getModelLoader());
        event.getModelRegistry().put(loc, model);
        event.getModelRegistry().put(loc0, model0);
    }
}
