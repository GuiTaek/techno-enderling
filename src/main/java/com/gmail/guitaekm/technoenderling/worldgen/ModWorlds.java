package com.gmail.guitaekm.technoenderling.worldgen;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

public class ModWorlds {
    public record LazyInformation(ServerWorld enderworld, ServerWorld pocketDimension) { }
    public static LazyInformation getInfo(MinecraftServer server) {
        ServerWorld enderworld = server.getWorld(
                RegistryKey.of(
                        Registry.WORLD_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "enderworld")
                )
        );
        ServerWorld pocketDimension = server.getWorld(
                RegistryKey.of(
                        Registry.WORLD_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "pocket_dimension")
                )
        );
        return new LazyInformation(enderworld, pocketDimension);
    }
}
