package com.gmail.guitaekm.technoenderling.features;

import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EnderlingStructureRegistry implements HandleLongUseServer.Listener {
    protected List<EnderlingStructure> structures = new ArrayList<>();
    protected static EnderlingStructureRegistry instance;
    public static void registerClass() {
        EnderlingStructureRegistry.instance = new EnderlingStructureRegistry();
        HandleLongUseServer.register(EnderlingStructureRegistry.instance());
    }
    public static EnderlingStructureRegistry instance() {
        return EnderlingStructureRegistry.instance;
    }
    public void register(EnderlingStructure structure) {
        this.structures.add(structure);
    }
    public void clear() {
        this.structures.clear();
    }

    public void deregister(Identifier id) {
        this.structures = this.structures.stream().filter(
                (EnderlingStructure structure) -> structure.getId() != id
        ).toList();
    }

    public void applyStructureTransformation(ServerWorld world, BlockPos pos) {
        // shuffle makes the applied transformation non-deterministic, if multiple possibilities
        Collections.shuffle(this.structures, world.getRandom());
        this.structures.forEach(
                (EnderlingStructure structure) -> {
                    Optional<BlockPos> toPlacePos = structure.convertible.findStructureToConvert(world, pos);
                    toPlacePos.ifPresent(blockPos -> structure.placeable.generate(world, blockPos));
                }
        );
    }

    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        applyStructureTransformation(player.getWorld(), pos);
    }
}
