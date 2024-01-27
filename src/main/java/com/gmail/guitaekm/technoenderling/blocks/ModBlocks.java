package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block INFUSED_GLOWSTONE = new Block(
            FabricBlockSettings
                    .of(Material.GLASS, MapColor.PALE_YELLOW)
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 15)
    );
    public static final Block INFUSED_GOLD_BLOCK = new Block(
            FabricBlockSettings
                    .of(Material.METAL, MapColor.GOLD)
                    .requiresTool()
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block ENDERWORLD_PORTAL_BLOCK_1 = new EnderworldPortalBlock(
            FabricBlockSettings
                    .of(Material.GLASS, MapColor.PALE_YELLOW)
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 15),
            1,
            false,
            ModBlocks.INFUSED_GLOWSTONE
    );
    public static final Block ENDERWORLD_PORTAL_BLOCK_2 = new EnderworldPortalBlock(
            FabricBlockSettings
                    .of(Material.METAL, MapColor.GOLD)
                    .requiresTool()
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.METAL),
            2,
            true,
            ModBlocks.INFUSED_GOLD_BLOCK
    );
    public static final Block ENDERWORLD_PORTAL_BLOCK_3 = new EnderworldPortalBlock(
            FabricBlockSettings
                    .of(Material.METAL, MapColor.GOLD)
                    .requiresTool()
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.METAL),
            3,
            true,
            ModBlocks.INFUSED_GOLD_BLOCK
    );
    public static void register() {
        Registry.register(
                Registry.BLOCK,
                new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal_block_1"),
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_1
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal_block_2"),
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_2
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal_block_3"),
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_3
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(TechnoEnderling.MOD_ID, "infused_glowstone"),
                ModBlocks.INFUSED_GLOWSTONE
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(TechnoEnderling.MOD_ID, "infused_gold_block"),
                ModBlocks.INFUSED_GOLD_BLOCK
        );
    }
}
