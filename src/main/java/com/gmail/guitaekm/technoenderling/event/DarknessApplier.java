package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.config.FogBiomes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;

import java.util.List;

public class DarknessApplier implements ServerTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftServer server) {
        List<ServerPlayerEntity> playersToProcess = server.getPlayerManager().getPlayerList().stream()
                .filter(
                        (PlayerEntity player)->FogBiomes.isFogBiome(
                                player
                                        .getWorld()
                                        .getBiomeKey(player.getBlockPos())
                                        .get()
                                        .getValue()
                                        .toString()
                        )
                ).toList();
        playersToProcess.stream()
                .forEach(
                        (PlayerEntity player)->{
                            player.addStatusEffect(
                                    new StatusEffectInstance(
                                            StatusEffects.BLINDNESS,
                                            40,
                                            0,
                                            true,
                                            false
                                    )
                            );
                        }
                );
    }
}
