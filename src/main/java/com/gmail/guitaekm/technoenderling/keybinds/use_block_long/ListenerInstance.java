package com.gmail.guitaekm.technoenderling.keybinds.use_block_long;

import net.minecraft.block.Block;

public class ListenerInstance {
    public int maxAge;
    public CallbackClient callback;
    public boolean dead;

    public ListenerInstance(int maxAge, CallbackClient callback, boolean dead) {
        this.maxAge = maxAge;
        this.callback = callback;
        this.dead = dead;
    }
}
