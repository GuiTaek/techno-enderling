package com.gmail.guitaekm.technoenderling.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block ENDERWORLD_PORTAL_BLOCK_1 = new EnderworldPortalBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f), 1);
    public static final Block ENDERWORLD_PORTAL_BLOCK_2 = new EnderworldPortalBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f), 2);
    public static final Block ENDERWORLD_PORTAL_BLOCK_3 = new EnderworldPortalBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f), 3);
    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier("technoenderling", "enderworld_portal_block_1"), ModBlocks.ENDERWORLD_PORTAL_BLOCK_1);
        Registry.register(Registry.ITEM, new Identifier("technoenderling", "enderworld_portal_block_1"), new BlockItem(ModBlocks.ENDERWORLD_PORTAL_BLOCK_1, new FabricItemSettings()));
        Registry.register(Registry.BLOCK, new Identifier("technoenderling", "enderworld_portal_block_2"), ModBlocks.ENDERWORLD_PORTAL_BLOCK_2);
        Registry.register(Registry.ITEM, new Identifier("technoenderling", "enderworld_portal_block_2"), new BlockItem(ModBlocks.ENDERWORLD_PORTAL_BLOCK_2, new FabricItemSettings()));
        Registry.register(Registry.BLOCK, new Identifier("technoenderling", "enderworld_portal_block_3"), ModBlocks.ENDERWORLD_PORTAL_BLOCK_3);
        Registry.register(Registry.ITEM, new Identifier("technoenderling", "enderworld_portal_block_3"), new BlockItem(ModBlocks.ENDERWORLD_PORTAL_BLOCK_3, new FabricItemSettings()));
    }
}
