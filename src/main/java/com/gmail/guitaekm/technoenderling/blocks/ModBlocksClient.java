package com.gmail.guitaekm.technoenderling.blocks;

public class ModBlocksClient {
    public static void register() {
        // have to be creative about how to split into server and client, because this probably won't work
        ModBlocks.ONE_WAY_PORTAL_BLOCK.registerClient();
    }
}
