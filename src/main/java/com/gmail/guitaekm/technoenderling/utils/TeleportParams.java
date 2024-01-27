package com.gmail.guitaekm.technoenderling.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class TeleportParams {
    final public ServerPlayerEntity player;
    final public ServerWorld targetWorld;
    // needed for the view direction
    final public BlockPos portalPos;
    final public double x;
    final public double y;
    final public List<ChunkPos> unloadedChunks;

    public TeleportParams(ServerPlayerEntity player, ServerWorld targetWorld, BlockPos portalPos, double x, double y, double z) {
        this.player = player;
        this.targetWorld = targetWorld;
        this.portalPos = portalPos;
        this.x = x;
        this.y = y;
        this.z = z;
        this.unloadedChunks = new ArrayList<>();
    }

    final public double z;
}
