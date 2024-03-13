package com.gmail.guitaekm.endergenesis.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyBlockEntity extends BlockEntity {
    public EmptyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.EMPTY_BLOCK_ENTITY, pos, state);
    }
    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity entity) { }
}
