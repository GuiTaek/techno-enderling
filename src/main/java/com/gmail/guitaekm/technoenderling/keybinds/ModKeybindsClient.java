package com.gmail.guitaekm.technoenderling.keybinds;

import com.gmail.guitaekm.technoenderling.keybinds.use_block_long.SendPacketToServer;
import com.gmail.guitaekm.technoenderling.keybinds.use_block_long.UseBlockLong;

public class ModKeybindsClient {
    public static void register() {
        UseBlockLong.registerListener(SendPacketToServer.MAX_AGE, new SendPacketToServer());
    }
}
