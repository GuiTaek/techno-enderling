package com.gmail.guitaekm.technoenderling.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import com.gmail.guitaekm.technoenderling.blocks.ModBlocks;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Inject(method="Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;", at = @At("HEAD"), cancellable = true)
    private static void registerMyGoldBlock(String id, Block block, CallbackInfoReturnable<Block> ci) {
        if (id == "gold_block") {
            ci.setReturnValue(Registry.register(Registry.BLOCK, id, ModBlocks.GOLD_BLOCK));
            ci.cancel();
        }
    }
    @Shadow
    private static Block register(String id, Block block) { return null; }
}
