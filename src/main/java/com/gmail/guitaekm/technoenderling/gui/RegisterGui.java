package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import com.gmail.guitaekm.technoenderling.networking.TeleportDestinations;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class RegisterGui {
    public static final ExtendedScreenHandlerType<TeleportScreenHandler> TELEPORT_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(
            TeleportScreenHandler::new
    );
    public static void registerServer() {
        Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier(TechnoEnderling.MOD_ID, "my_test_screen_handler"),
                TELEPORT_SCREEN_HANDLER
        );
    }
    public static void registerClient() {
        ScreenRegistry.register(TELEPORT_SCREEN_HANDLER, TeleportScreen::new);
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if(MinecraftClient.getInstance().currentScreen instanceof TeleportScreen teleportScreen) {
                teleportScreen.updateMatrices(context.matrixStack(), context.projectionMatrix());
            }
        });
    }
}
