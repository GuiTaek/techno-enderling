package com.gmail.guitaekm.technoenderling.networking;

import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier LONG_USE_BLOCK = new Identifier("technoenderling", "long_use_block");
    public static void registerNetworkingServer() {
        HandleLongUseServer.registerServer();
    }
    public static void registerNetworkingClient() {

    }
}
