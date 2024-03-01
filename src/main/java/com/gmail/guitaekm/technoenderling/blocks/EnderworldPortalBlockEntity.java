package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.event.PortalPropagation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderworldPortalBlockEntity extends BlockEntity {
    public EnderworldPortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.ENDERWORLD_PORTAL_BLOCK_ENTITY_BLOCK_ENTITY, pos, state);
    }
    public static void tick(World world, BlockPos pos, BlockState state, EnderworldPortalBlockEntity entity) {
        if (world.isClient) {
            return;
        }
        PortalPropagation.propagateEnderworldPortal((ServerWorld) world, world.getWorldChunk(pos), PortalPropagation::loadEnderworldChunk);
    }
}
