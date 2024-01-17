package com.gmail.guitaekm.technoenderling.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UseBlockLong {

    static protected class SavedUsage {
        public Block block;
        public int age;

        SavedUsage(Block block, int age) {
            this.block = block;
            this.age = age;
        }
    }

    static protected class ListenerInstance {
        public Block block;
        public int maxAge;
        public Callback callback;
        public boolean dead;
        ListenerInstance(Block block, int maxAge, Callback callback, boolean dead) {
            this.block = block;
            this.maxAge = maxAge;
            this.callback = callback;
            this.dead = dead;
        }
    }
    public interface Callback {
        void onStartUse(MinecraftClient client, World world, PlayerEntity player);
        void onUseTick(MinecraftClient client, World world, PlayerEntity player, int age);
        void onEndUse(MinecraftClient client, World world, PlayerEntity player, int age);
    }
    final private static List<ListenerInstance> listeners = new ArrayList<>();
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
    public static int registerListener(Block block, int maxAge, Callback callback) {
        UseBlockLong.listeners.add(new ListenerInstance(block, maxAge, callback, false));
        return UseBlockLong.listeners.size() - 1;
    }
    protected static Optional<Block> getTargetBlock(MinecraftClient client) {
        BlockHitResult result = (BlockHitResult)client.crosshairTarget;
        if (result == null || client.world == null) {
            // don't ask me why the main menu is already ticking
            return Optional.empty();
        }
        Block targetBlock = client.world.getBlockState(result.getBlockPos()).getBlock();
        return Optional.of(targetBlock);
    }
    protected static void refreshListenerInstances() {
        UseBlockLong.listeners.forEach((ListenerInstance listener) -> listener.dead = false);
    }
    protected static List<ListenerInstance> iterateOverCallbacks(Block targetBlock) {
        if (current == null) {
            return new ArrayList<>();
        }
        List<ListenerInstance> listeners = new ArrayList<>();
        for(ListenerInstance entry : UseBlockLong.listeners) {
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
        for(ListenerInstance listener : UseBlockLong.iterateOverCallbacks(targetBlockOptional.get())) {
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
        for(ListenerInstance listener : UseBlockLong.iterateOverCallbacks(targetBlockOptional.get())) {
            listener.callback.onUseTick(client, client.world, client.player, current.age);
        }
    }
    protected static void callAllOnEndUse(MinecraftClient client) {
        if (UseBlockLong.current == null) {
            return;
        }
        if (UseBlockLong.hasSameTarget(client)) {
            for(ListenerInstance listener : UseBlockLong.iterateOverCallbacks(current.block)) {
                if (current.age == listener.maxAge) {
                    listener.callback.onEndUse(client, client.world, client.player, current.age);
                    listener.dead = true;
                }
            }
            return;
        }
        for(ListenerInstance listener : UseBlockLong.iterateOverCallbacks(current.block)) {
            listener.callback.onEndUse(client, client.world, client.player, current.age);
            listener.dead = true;
        }
    }
}
