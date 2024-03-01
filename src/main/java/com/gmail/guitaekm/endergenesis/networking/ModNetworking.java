package com.gmail.guitaekm.endergenesis.networking;

import com.gmail.guitaekm.endergenesis.EnderGenesis;
import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier LONG_USE_BLOCK = new Identifier(EnderGenesis.MOD_ID, "long_use_block");
    public static final Identifier ASK_WAITING_MOUNTING = new Identifier(EnderGenesis.MOD_ID, "ask_waiting_mounting");
    public static final Identifier MOUNTING_READY = new Identifier(EnderGenesis.MOD_ID, "mounting_ready");
    public static final Identifier REQUEST_NETHER_TELEPORT = new Identifier(EnderGenesis.MOD_ID, "request_nether_teleport");
    public static final Identifier ANSWER_RENAMING_REQUEST = new Identifier(EnderGenesis.MOD_ID, "answer_renaming_request");
    public static void registerNetworkingServer() {
        HandleLongUseServer.registerServer();
        AcceptFinishWaitingMount.register();
    }
    public static void registerNetworkingClient() {
        WaitMounting.register();
    }
}
