package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.access.IChunkTicketManagerAccess;
import com.gmail.guitaekm.technoenderling.mixin.ChunkTicketManagerMixin;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import com.gmail.guitaekm.technoenderling.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.technoenderling.worldgen.ModWorlds;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.collection.SortedArraySet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class PortalPropagation {
    public static final ChunkTicketType<Unit> ENDERWORLD_PORTAL_TICKET = ChunkTicketType.create(
            "start", (left, right) -> 0, 5
    );
    public PortalPropagation() { }
    public static Set<ChunkPos> propagatedEnderworldPortalOverworld = new HashSet<>();
    public static void propagateEnderworldPortal(
            ServerWorld world,
            WorldChunk chunk,
            BiConsumer<MinecraftServer, ChunkPos> consumer
    ) {
        ChunkStatus.ChunkType t = chunk.getStatus().getChunkType();
        if (!world.getRegistryKey().getValue().equals(new Identifier("minecraft:overworld"))) {
            return;
        }
        world.getPointOfInterestStorage().getInChunk(
                ModPointsOfInterest.IS_ENDERWORLD_PORTAL,
                chunk.getPos(),
                PointOfInterestStorage.OccupationStatus.ANY
        ).forEach(pointOfInterest -> {
            BlockPos pos = pointOfInterest.getPos();
            if (chunk.getBlockState(pos).get(EnderworldPortalBlock.GENERATED)) {
                return;
            }
            List<? extends ChunkTicketType<?>> set = ((IChunkTicketManagerAccess) world.getChunkManager().threadedAnvilChunkStorage.getTicketManager())
                    .techno_enderling$getTicketSetPublic(new ChunkPos(pos).toLong())
                    .stream()
                    .map(ChunkTicket::getType)
                    .toList();
            if (set.isEmpty()) {
                // I don't understand this case
                return;
            }
            if (set.contains(ChunkTicketType.START)) {
                return;
            }
            consumer.accept(world.getServer(), toLoadChunk(world.getServer(), pos));
        });
    }
    public static ChunkPos toLoadChunk(MinecraftServer server, BlockPos portalPos) {
        BlockPos toLoad = EnderworldPortalBlock.overworldToEnderworldMiddle(server, portalPos);
        return new ChunkPos(toLoad);
    }
    public static void loadEnderworldChunk(MinecraftServer server, ChunkPos toLoadChunk) {
        propagatedEnderworldPortalOverworld.add(toLoadChunk);
        ModWorlds
                .getInfo(server)
                .enderworld()
                .getChunkManager()
                .addTicket(ENDERWORLD_PORTAL_TICKET, toLoadChunk, 1, Unit.INSTANCE);
    }

    public static void unloadEnderworldChunk(MinecraftServer server, ChunkPos toLoadChunk) {
        propagatedEnderworldPortalOverworld.remove(toLoadChunk);
        ModWorlds
                .getInfo(server)
                .enderworld()
                .getChunkManager()
                .removeTicket(ENDERWORLD_PORTAL_TICKET, toLoadChunk, 1, Unit.INSTANCE);
    }

}
