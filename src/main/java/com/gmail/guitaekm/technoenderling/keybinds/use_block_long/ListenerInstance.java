package com.gmail.guitaekm.technoenderling.keybinds.use_block_long;

import net.minecraft.block.Block;

public class ListenerInstance {
    public Block block;
    public int maxAge;
    public Callback callback;
    public boolean dead;

    public ListenerInstance(Block block, int maxAge, Callback callback, boolean dead) {
        this.block = block;
        this.maxAge = maxAge;
        this.callback = callback;
        this.dead = dead;
    }
}
