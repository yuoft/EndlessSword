package com.yuo.es;

import com.yuo.es.Entity.EsEntityTypes;
import com.yuo.es.Items.ESItems;
import com.yuo.es.Proxy.ClientProxy;
import com.yuo.es.Proxy.CommonProxy;
import com.yuo.es.Proxy.IProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("endless_sword")
public class EndlessSword {
	public static final String MOD_ID = "endless_sword";
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public EndlessSword() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

		//注册至mod总线
        ESItems.ITEMS.register(modEventBus);
        EsEntityTypes.ENTITY_TYPES.register(modEventBus);
        proxy.registerHandlers();

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
