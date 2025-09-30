package com.yuo.es.Proxy;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IProxy {
    default void registerHandlers(IEventBus modBus) {}
}
