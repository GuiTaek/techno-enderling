package com.gmail.guitaekm.technoenderling.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEventsServer {
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(new DarknessApplierServer());
        LinkEnderworldPortals.getInstance().registerClass();
    }
}
