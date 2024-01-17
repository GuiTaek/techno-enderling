package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.keybinds.use_block_long.UseBlockLong;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import com.gmail.guitaekm.technoenderling.keybinds.use_block_long.Callback;

public class ModifiedGoldBlock extends Block implements Callback {
    public ModifiedGoldBlock(Settings settings) {
        super(settings);
        UseBlockLong.registerListener(this, 80, this);
    }

    @Override
    public void onStartUse(MinecraftClient client, World world, PlayerEntity player) {
        player.giveItemStack(new ItemStack(Items.DIAMOND, 1));
    }

    @Override
    public void onUseTick(MinecraftClient client, World world, PlayerEntity player, int age) {
        player.giveItemStack(new ItemStack(Items.STICK, 1));
    }

    @Override
    public void onEndUse(MinecraftClient client, World world, PlayerEntity player, int age) {
        player.giveItemStack(new ItemStack(Items.EMERALD, 1));
    }
}
