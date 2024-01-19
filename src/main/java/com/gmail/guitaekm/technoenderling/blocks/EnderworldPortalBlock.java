package com.gmail.guitaekm.technoenderling.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;

public class EnderworldPortalBlock extends Block {
    final public int index;

    public EnderworldPortalBlock(Settings settings, int index) {
        super(settings);
        this.index = index;
    }
}
