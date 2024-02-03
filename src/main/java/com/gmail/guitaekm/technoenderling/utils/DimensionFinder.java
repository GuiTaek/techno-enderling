package com.gmail.guitaekm.technoenderling.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;

public class DimensionFinder {
    final protected Identifier id;
    protected RegistryKey<World> registryKey;
    public DimensionFinder(Identifier dimensionId) {
        this.id = dimensionId;
    }
    public void lazyInit(MinecraftServer server) {
        this.registryKey = server
            .getWorldRegistryKeys()
                .stream()
                .filter(
                        (RegistryKey<World> registryKey) -> registryKey.getValue().equals(this.id)
                )
                .findFirst()
                .get();
    }
    public RegistryKey<World> get() {
        return this.registryKey;
    }
}
