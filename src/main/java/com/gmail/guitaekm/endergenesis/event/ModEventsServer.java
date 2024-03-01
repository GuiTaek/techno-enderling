package com.gmail.guitaekm.endergenesis.event;

import com.gmail.guitaekm.endergenesis.enderling_structure.LinkEnderworldPortals;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEventsServer {
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(new DarknessApplierServer());
        new LinkEnderworldPortals().register();
        // register is inside the constructor
        new PortalPropagation();
    }
}
