package com.gmail.guitaekm.technoenderling.enderling_structure;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.event.EnderlingStructureEvents;
import com.gmail.guitaekm.technoenderling.utils.ArbitraryStructure;
import com.gmail.guitaekm.technoenderling.worldgen.ModWorlds;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkGeneratedEnderworldPortal
{
    public static void generateOtherPortal(MinecraftServer server, ArbitraryStructure portal, BlockPos root) {
        ServerWorld enderworld = ModWorlds.getInfo(server).enderworld();
        assert enderworld != null;
        BlockPos toPlace = LinkEnderworldPortals.overworldToEnderworldRandom(enderworld, root);
        // link to the surface
        // this method only works when the chunk is loaded, see World.getTopY and as it isn't as easy
        // to load the world on the fly, I'll just grep the part of World.getTopY that I need
        // toPlace = enderworld.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, toPlace);
        int x = toPlace.getX();
        int z = toPlace.getZ();
        int newY = enderworld.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z)).sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x & 15, z & 15);
        toPlace = toPlace.withY(newY);
        portal.place(enderworld, toPlace, new Vec3i(0, 0, 0), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
    }
}
