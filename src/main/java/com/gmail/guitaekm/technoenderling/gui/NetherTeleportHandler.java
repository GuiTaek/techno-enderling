package com.gmail.guitaekm.technoenderling.gui;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;

public class NetherTeleportHandler {
    public static void registerClient() {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if(MinecraftClient.getInstance().currentScreen instanceof TeleportScreen teleportScreen) {
                teleportScreen.updateMatrices(context.matrixStack(), context.projectionMatrix());
            }
        });
    }
}
