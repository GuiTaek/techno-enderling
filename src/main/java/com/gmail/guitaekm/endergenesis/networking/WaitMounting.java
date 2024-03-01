package com.gmail.guitaekm.endergenesis.networking;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;

public class WaitMounting {
    protected static WaitMountingPacket packet;
    public static void register() {
        ClientTickEvents.END_WORLD_TICK.register(new ClientTickEvents.EndWorldTick() {
            @Override
            public void onEndTick(ClientWorld world) {
                if (WaitMounting.packet == null) {
                    return;
                }
                if (WaitMounting.packet.checkReady(world)) {
                    ClientPlayNetworking.send(ModNetworking.MOUNTING_READY, PacketByteBufs.create());
                    WaitMounting.packet = null;
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.ASK_WAITING_MOUNTING, new ClientPlayNetworking.PlayChannelHandler() {
            @Override
            public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
                WaitMounting.packet = new WaitMountingPacket(buf);
            }
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(new ClientLifecycleEvents.ClientStopping() {
            @Override
            public void onClientStopping(MinecraftClient client) {
                WaitMounting.packet = null;
            }
        });
    }
}
