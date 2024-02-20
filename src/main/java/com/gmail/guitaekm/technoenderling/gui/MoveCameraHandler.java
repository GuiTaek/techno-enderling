package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.access.IMouseMixin;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.WorldChunk;

public class MoveCameraHandler implements ClientChunkEvents.Load, WorldRenderEvents.DebugRender {
    private ClientWorld world = null;
    public void register() {
        ClientChunkEvents.CHUNK_LOAD.register(this);
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this);
    }

    @Override
    public void onChunkLoad(ClientWorld world, WorldChunk chunk) {
        this.world = world;
    }

    @Override
    public void beforeDebugRender(WorldRenderContext context) {
        if(!(MinecraftClient.getInstance().currentScreen instanceof TeleportScreen)) {
            return;
        }
        // temporarily here
        ((IMouseMixin) MinecraftClient.getInstance().mouse).setKeepScreen();
        MinecraftClient.getInstance().mouse.lockCursor();
        ((IMouseMixin) MinecraftClient.getInstance().mouse).unsetKeepScreen();
    }
}
