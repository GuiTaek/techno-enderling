package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.enderling_structure.LinkGeneratedEnderworldPortal;
import com.gmail.guitaekm.technoenderling.enderling_structure.LinkEnderworldPortals;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEventsServer {
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(new DarknessApplierServer());
        new LinkEnderworldPortals().register();
    }
}
