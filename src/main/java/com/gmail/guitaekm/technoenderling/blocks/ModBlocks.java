package com.gmail.guitaekm.technoenderling.blocks;

import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {
    public static final Block GOLD_BLOCK = new ModifiedGoldBlock(
            AbstractBlock
                    .Settings
                    .of(Material.METAL, MapColor.GOLD)
                    .requiresTool()
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static void register() {

    }
}
