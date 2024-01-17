package com.gmail.guitaekm.technoenderling.keybinds;

import net.minecraft.block.Block;

public class UseBlockLongListenerInstance {
    public Block block;
    public int maxAge;
    public UseBlockLongCallback callback;
    public boolean dead;

    public UseBlockLongListenerInstance(Block block, int maxAge, UseBlockLongCallback callback, boolean dead) {
        this.block = block;
        this.maxAge = maxAge;
        this.callback = callback;
        this.dead = dead;
    }
}
