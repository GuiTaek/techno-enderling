package com.gmail.guitaekm.technoenderling.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UseBlockLong {

    static public class SavedUsage {
        public Block block;
        public int age;

        public SavedUsage(Block block, int age) {
            this.block = block;
            this.age = age;
        }
    }

    final private static List<UseBlockLongListenerInstance> listeners = new ArrayList<>();
    private static @Nullable SavedUsage current = null;
    public static void registerClass() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.options.keyUse.isPressed()) {
                if (UseBlockLong.hasNewTarget(client)) {
                    UseBlockLong.callAllOnStartUse(client);
                    UseBlockLong.callAllOnUseTick(client);
                    return;
                }
                UseBlockLong.callAllOnUseTick(client);
                UseBlockLong.callAllOnEndUse(client);
                if (UseBlockLong.hasSameTarget(client)) {
                    if (UseBlockLong.current == null) {
                        throw new IllegalArgumentException("The method before should eliminate current being null. However, that's not the case");
                    }
                    UseBlockLong.current.age++;
                }
                return;
            }
            UseBlockLong.callAllOnEndUse(client);
            UseBlockLong.refreshListenerInstances();
            UseBlockLong.current = null;
        });
    }
    public static UseBlockLongListenerInstance registerListener(Block block, int maxAge, UseBlockLongCallback callback) {
        UseBlockLongListenerInstance listener = new UseBlockLongListenerInstance(block, maxAge, callback, false);
        UseBlockLong.listeners.add(listener);
        return listener;
    }

    public static boolean deregisterListener(UseBlockLongListenerInstance listener) {
        return UseBlockLong.listeners.remove(listener);
    }
    protected static Optional<Block> getTargetBlock(MinecraftClient client) {
        if (client.crosshairTarget == null) {
            return Optional.empty();
        }
        if (!(client.crosshairTarget instanceof BlockHitResult result)) {
            return Optional.empty();
        }
        if (client.world == null) {
            // don't ask me why the main menu is already ticking
            return Optional.empty();
        }
        Block targetBlock = client.world.getBlockState(result.getBlockPos()).getBlock();
        return Optional.of(targetBlock);
    }
    protected static void refreshListenerInstances() {
        UseBlockLong.listeners.forEach((UseBlockLongListenerInstance listener) -> listener.dead = false);
    }
    protected static List<UseBlockLongListenerInstance> iterateOverCallbacks(Block targetBlock) {
        if (current == null) {
            return new ArrayList<>();
        }
        List<UseBlockLongListenerInstance> listeners = new ArrayList<>();
        for(UseBlockLongListenerInstance entry : UseBlockLong.listeners) {
            if (current.age <= entry.maxAge) {
                if (entry.block == targetBlock) {
                    if (!entry.dead) {
                        listeners.add(entry);
                    }
                }
            }
        }
        return listeners;
    }
    protected static boolean hasNewTarget(MinecraftClient client) {
        if (!client.options.keyUse.isPressed()) {
            return false;
        }
        Optional<Block> targetBlockOptional = UseBlockLong.getTargetBlock(client);
        if (targetBlockOptional.isEmpty()) {
            return false;
        }
        return current == null;
    }
    protected static boolean hasSameTarget(MinecraftClient client) {
        if (!client.options.keyUse.isPressed()) {
            return false;
        }
        Optional<Block> targetBlockOptional = UseBlockLong.getTargetBlock(client);
        if (targetBlockOptional.isEmpty()) {
            return false;
        }
        if (current == null) {
            return false;
        }
        return targetBlockOptional.get() == current.block;
    }
    protected static void callAllOnStartUse(MinecraftClient client) {
        if (UseBlockLong.hasSameTarget(client)) {
            return;
        }
        Optional<Block> targetBlockOptional = UseBlockLong.getTargetBlock(client);
        if (targetBlockOptional.isEmpty()) {
            return;
        }
        current = new SavedUsage(targetBlockOptional.get(), 0);
        for(UseBlockLongListenerInstance listener : UseBlockLong.iterateOverCallbacks(targetBlockOptional.get())) {
            listener.callback.onStartUse(client, client.world, client.player);
        }
    }
    protected static void callAllOnUseTick(MinecraftClient client) {
        if (!UseBlockLong.hasSameTarget(client)) {
            return;
        }
        if (current == null) {
            throw new IllegalArgumentException("The method before should eliminate current being null. However, that's not the case");
        }
        Optional<Block> targetBlockOptional = UseBlockLong.getTargetBlock(client);
        if (targetBlockOptional.isEmpty()) {
            // shouldn't happen, but safeguard
            throw new IllegalArgumentException();
        }
        for(UseBlockLongListenerInstance listener : UseBlockLong.iterateOverCallbacks(targetBlockOptional.get())) {
            listener.callback.onUseTick(client, client.world, client.player, current.age);
        }
    }
    protected static void callAllOnEndUse(MinecraftClient client) {
        if (UseBlockLong.current == null) {
            return;
        }
        if (UseBlockLong.hasSameTarget(client)) {
            for(UseBlockLongListenerInstance listener : UseBlockLong.iterateOverCallbacks(current.block)) {
                if (current.age == listener.maxAge) {
                    listener.callback.onEndUse(client, client.world, client.player, current.age);
                    listener.dead = true;
                }
            }
            return;
        }
        for(UseBlockLongListenerInstance listener : UseBlockLong.iterateOverCallbacks(current.block)) {
            listener.callback.onEndUse(client, client.world, client.player, current.age);
            listener.dead = true;
        }
    }
}
