package com.gmail.guitaekm.technoenderling.networking;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier LONG_USE_BLOCK = new Identifier("technoenderling", "long_use_block");
    public static final Identifier ASK_WAITING_MOUNTING = new Identifier(TechnoEnderling.MOD_ID, "ask_waiting_mounting");
    public static final Identifier MOUNTING_READY = new Identifier(TechnoEnderling.MOD_ID, "mounting_ready");
    public static final Identifier SHOW_TELEPORT_SCREEN = new Identifier(TechnoEnderling.MOD_ID, "show_teleport_screen");
    public static void registerNetworkingServer() {
        HandleLongUseServer.registerServer();
        AcceptFinishWaitingMount.register();
        ShowTeleportScreen.register();
    }
    public static void registerNetworkingClient() {
        WaitMounting.register();
    }
}
