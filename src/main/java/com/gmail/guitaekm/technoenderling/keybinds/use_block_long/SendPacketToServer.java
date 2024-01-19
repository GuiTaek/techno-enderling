package com.gmail.guitaekm.technoenderling.keybinds.use_block_long;

import com.gmail.guitaekm.technoenderling.networking.ModNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SendPacketToServer implements CallbackClient {
    public static final int MAX_AGE = 60;

    @Override
    public void onStartUse(MinecraftClient client, World world, PlayerEntity player, BlockPos pos) {

    }

    @Override
    public void onUseTick(MinecraftClient client, World world, PlayerEntity player, BlockPos pos, int age) {

    }

    @Override
    public void onEndUse(MinecraftClient client, World world, PlayerEntity player, BlockPos pos, int age) {
        if (age < MAX_AGE) {
            return;
        }
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(ModNetworking.LONG_USE_BLOCK, buf);
    }
}
