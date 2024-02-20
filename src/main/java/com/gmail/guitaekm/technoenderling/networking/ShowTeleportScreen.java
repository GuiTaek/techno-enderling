package com.gmail.guitaekm.technoenderling.networking;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;

public class ShowTeleportScreen {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                ModNetworking.SHOW_TELEPORT_SCREEN,
                (client, handler, buf, responseSender) -> {
                    TechnoEnderling.LOGGER.info("Hi, it works!");
                });
    }
}
