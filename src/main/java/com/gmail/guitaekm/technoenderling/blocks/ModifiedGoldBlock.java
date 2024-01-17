package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.keybinds.UseBlockLong;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModifiedGoldBlock extends Block implements UseBlockLong.Callback {
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
