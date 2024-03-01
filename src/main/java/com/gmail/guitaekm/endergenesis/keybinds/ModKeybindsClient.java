package com.gmail.guitaekm.endergenesis.keybinds;

import com.gmail.guitaekm.endergenesis.keybinds.use_block_long.SendPacketToServer;
import com.gmail.guitaekm.endergenesis.keybinds.use_block_long.UseBlockLong;

public class ModKeybindsClient {
    public static void register() {
        UseBlockLong.registerListener(SendPacketToServer.MAX_AGE, new SendPacketToServer());
    }
}
