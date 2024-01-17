package com.gmail.guitaekm.technoenderling.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface UseBlockLongCallback {
    void onStartUse(MinecraftClient client, World world, PlayerEntity player);

    void onUseTick(MinecraftClient client, World world, PlayerEntity player, int age);

    void onEndUse(MinecraftClient client, World world, PlayerEntity player, int age);
}
