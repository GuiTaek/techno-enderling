package com.gmail.guitaekm.endergenesis.blocks;

import com.gmail.guitaekm.endergenesis.event.PortalPropagation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderworldPortalBlockEntity extends BlockEntity {
    public EnderworldPortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.ENDERWORLD_PORTAL_BLOCK_ENTITY, pos, state);
    }
    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity entity) {
        if (world.isClient) {
            return;
        }
        if (state.getBlock() instanceof EnderworldPortalBlock enderworldPortalBlock) {
            if (enderworldPortalBlock.index != 3) {
                assert false;
                return;
            }
            PortalPropagation.propagatePortalTicket((ServerWorld) world, world.getWorldChunk(pos), pos);
        }
    }
}
