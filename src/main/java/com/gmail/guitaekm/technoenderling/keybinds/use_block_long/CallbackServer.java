package com.gmail.guitaekm.technoenderling.keybinds.use_block_long;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public interface CallbackServer {
    void onEndUse(MinecraftServer server, World world, PlayerEntity player, BlockPos pos);
}
