package com.gmail.guitaekm.endergenesis.keybinds.use_block_long;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CallbackClient {
    void onStartUse(MinecraftClient client, World world, PlayerEntity player, BlockPos pos);

    void onUseTick(MinecraftClient client, World world, PlayerEntity player, BlockPos pos, int age);

    void onEndUse(MinecraftClient client, World world, PlayerEntity player, BlockPos pos, int age);
}
