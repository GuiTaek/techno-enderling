package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureFeature;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureFeatureConfig;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import com.gmail.guitaekm.technoenderling.utils.StructureIter;
import com.gmail.guitaekm.technoenderling.utils.VehicleTeleport;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LinkEnderworldPortalOnGenerate implements OnStructureGenerate.Listener, ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopped {
    final private static LinkEnderworldPortalOnGenerate instance = new LinkEnderworldPortalOnGenerate();
    private MinecraftServer server;
    public static LinkEnderworldPortalOnGenerate getInstance() {
        return LinkEnderworldPortalOnGenerate.instance;
    }

    public static void register() {
        OnStructureGenerate.getInstance().register(LinkEnderworldPortalOnGenerate.getInstance());
        ServerLifecycleEvents.SERVER_STARTED.register(LinkEnderworldPortalOnGenerate.getInstance());
        ServerLifecycleEvents.SERVER_STOPPED.register(LinkEnderworldPortalOnGenerate.getInstance());
    }

    public void beLazy(WorldAccess world, EnderlingStructure portal, BlockPos rootOverworld) {
        if (!portal.getId().equals(new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal"))) {
            return;
        }
        this.portal = portal;
        this.toGenerate.add(rootOverworld);
        this.overworld = world;
    }

    @Override
    public ActionResult onStructureGenerate(WorldAccess world, EnderlingStructure portal, BlockPos root) {
        if (this.server == null || this.server.isStopped()) {
            this.server = null;
            this.beLazy(world, portal, root);
            return ActionResult.PASS;
        }
        generateOtherPortal(world, portal, root);
        return ActionResult.PASS;
    }

    private @Nullable EnderlingStructure portal;
    private @Nullable WorldAccess overworld;
    final private List<BlockPos> toGenerate;

    public void generateOtherPortal(WorldAccess world, EnderlingStructure portal, BlockPos root) {
        DimensionFinder f = new DimensionFinder(new Identifier(TechnoEnderling.MOD_ID, "enderworld"));
        f.lazyInit(this.server);
        assert world.getServer() != null;
        ServerWorld enderworld = world.getServer().getWorld(f.get());
        assert enderworld != null;
        BlockPos toPlace = LinkEnderworldPortals.overworldToEnderworldRandom(enderworld, root);
        // link to the surface
        // this method only works when the chunk is loaded, see World.getTopY and as it isn't as easy
        // to load the world on the fly, I'll just grep the part of World.getTopY that I need
        // toPlace = enderworld.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, toPlace);
        int x = toPlace.getX();
        int z = toPlace.getZ();
        int newY = enderworld.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z)).sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x & 15, z & 15) + 2;
        toPlace = toPlace.withY(newY);
        Vec3i sizeOffset = portal.getPlaceable().size().add(new Vec3i(-1, -2, -1));
        toPlace = toPlace.add(EnderlingStructureFeature.config.offset())
                .add(sizeOffset);
        portal.getPlaceable().generate(enderworld, toPlace.withY(newY));
    }
    private LinkEnderworldPortalOnGenerate() {
        this.server = null;
        this.portal = null;
        this.overworld = null;
        this.toGenerate = new ArrayList<>();
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
        this.server = server;
        if (this.toGenerate.isEmpty()) {
            return;
        }
        assert this.overworld != null;
        assert this.portal != null;
        for (BlockPos root : this.toGenerate) {
            this.generateOtherPortal(this.overworld, this.portal, root);
        }
        this.toGenerate.clear();
    }

    @Override
    public void onServerStopped(MinecraftServer server) {
        this.server = null;
        this.toGenerate.clear();
        this.overworld = null;
        this.portal = null;
    }
}
