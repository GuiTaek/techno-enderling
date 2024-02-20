package com.gmail.guitaekm.technoenderling.networking;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier LONG_USE_BLOCK = new Identifier(TechnoEnderling.MOD_ID, "long_use_block");
    public static final Identifier ASK_WAITING_MOUNTING = new Identifier(TechnoEnderling.MOD_ID, "ask_waiting_mounting");
    public static final Identifier MOUNTING_READY = new Identifier(TechnoEnderling.MOD_ID, "mounting_ready");
    public static final Identifier TELEPORT_DESTINATIONS = new Identifier(TechnoEnderling.MOD_ID, "teleport_destinations");
    public static void registerNetworkingServer() {
        HandleLongUseServer.registerServer();
        AcceptFinishWaitingMount.register();
    }
    public static void registerNetworkingClient() {
        WaitMounting.register();
    }
}
