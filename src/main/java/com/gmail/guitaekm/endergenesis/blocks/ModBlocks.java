package com.gmail.guitaekm.endergenesis.blocks;

import com.gmail.guitaekm.endergenesis.EnderGenesis;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
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
    public static final BlockEntityType<EnderworldPortalBlockEntity> ENDERWORLD_PORTAL_BLOCK_ENTITY_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(EnderGenesis.MOD_ID, "enderworld_portal_block_entity"),
            FabricBlockEntityTypeBuilder
                    .create(EnderworldPortalBlockEntity::new, ENDERWORLD_PORTAL_BLOCK_3)
                    .build()
    );
    public static final PocketPortalBlock POCKET_PORTAL_BLOCK = new PocketPortalBlock(
            FabricBlockSettings
                    .of(Material.STONE)
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning((state, world, pos, type) -> false)
    );
    public static final OneWayPortal ONE_WAY_PORTAL_BLOCK = new OneWayPortal(
            FabricBlockSettings
                    .of(Material.GLASS)
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning((state, world, pos, type) -> false)
                    .blockVision((state, world, pos) -> false)
                    .nonOpaque()

    );
    public static void register() {
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "enderworld_portal_block_1"),
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_1
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "enderworld_portal_block_2"),
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_2
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "enderworld_portal_block_3"),
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_3
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "infused_glowstone"),
                ModBlocks.INFUSED_GLOWSTONE
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "infused_gold_block"),
                ModBlocks.INFUSED_GOLD_BLOCK
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "pocket_portal_block"),
                ModBlocks.POCKET_PORTAL_BLOCK
        );
        Registry.register(
                Registry.BLOCK,
                new Identifier(EnderGenesis.MOD_ID, "one_way_portal_block"),
                ModBlocks.ONE_WAY_PORTAL_BLOCK
        );
        // removes information that will be outdated when the server stops but the game continue
        EnderworldPortalBlock.register();
        PocketPortalBlock.register(ModBlocks.POCKET_PORTAL_BLOCK);
        ONE_WAY_PORTAL_BLOCK.registerServer();
    }
}
