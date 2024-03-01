package com.gmail.guitaekm.technoenderling.worldgen;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

public class ModWorlds {

    public static RegistryKey<World> enderworldKey = RegistryKey.of(
            Registry.WORLD_KEY,
            new Identifier(TechnoEnderling.MOD_ID, "enderworld")
    );

    public static RegistryKey<World> pocketDimensionKey = RegistryKey.of(
            Registry.WORLD_KEY,
            new Identifier(TechnoEnderling.MOD_ID, "pocket_dimension")
    );
    public record LazyInformation(
            ServerWorld enderworld,
            ServerWorld pocketDimension,
            RegistryKey<World> enderworldKey,
            RegistryKey<World> pocketDimensionKey
    ) { }
    public static LazyInformation getInfo(MinecraftServer server) {
        ServerWorld enderworld = server.getWorld(enderworldKey);
        ServerWorld pocketDimension = server.getWorld(pocketDimensionKey);
        return new LazyInformation(enderworld, pocketDimension, enderworldKey, pocketDimensionKey);
    }
}
