package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.resources.FogBiomes;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class DarknessApplierClient implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (FogBiomes.isFogBiome(
                client.player
                        .getWorld()
                        .getBiomeKey(client.player.getBlockPos())
                        .get()
                        .getValue()
                        .toString()
        )) {
            client.player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.BLINDNESS,
                            40,
                            0,
                            true,
                            false
                    )
            );
        }
    }
}
