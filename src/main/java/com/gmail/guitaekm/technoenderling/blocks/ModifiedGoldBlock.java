package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.keybinds.use_block_long.UseBlockLong;
import com.gmail.guitaekm.technoenderling.networking.ModNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.gmail.guitaekm.technoenderling.keybinds.use_block_long.CallbackClient;

public class ModifiedGoldBlock extends Block implements CallbackClient {
    public static int MAX_AGE = 60;
    public ModifiedGoldBlock(Settings settings) {
        super(settings);
        UseBlockLong.registerListener(ModifiedGoldBlock.MAX_AGE, this);
    }

    @Override
    public void onStartUse(MinecraftClient client, World world, PlayerEntity player, BlockPos pos) {
    }

    @Override
    public void onUseTick(MinecraftClient client, World world, PlayerEntity player, BlockPos pos, int age) {
    }

    @Override
    public void onEndUse(MinecraftClient client, World world, PlayerEntity player, BlockPos pos, int age) {
    }
}
