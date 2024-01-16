package com.gmail.guitaekm.technoenderling.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

public class ModEvents {
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(new DarknessApplier());
    }
}
