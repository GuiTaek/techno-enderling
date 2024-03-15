package com.gmail.guitaekm.endergenesis.blocks;

import com.gmail.guitaekm.endergenesis.event.PortalPropagation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OneWayPortalEntity extends BlockEntity {
    public OneWayPortalEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.ONE_WAY_PORTAL_ENTITY, pos, state);
    }
    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity entity) {
        if (world.isClient) {
            return;
        }
        if (state.getBlock() instanceof OneWayPortal) {
            PortalPropagation.propagateOneWayPortalTicket(world.getServer(), (ServerWorld) world, pos);
        }
    }
}
