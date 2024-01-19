package com.gmail.guitaekm.technoenderling.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ModEventsClient {
    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(new DarknessApplierClient());
    }
}
