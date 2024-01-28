package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import com.gmail.guitaekm.technoenderling.utils.TeleportParams;
import com.gmail.guitaekm.technoenderling.utils.VehicleTeleport;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class EnderworldPortalBlock extends Block implements HandleLongUseServer.Listener {
    // configure
    public static final int[][] RESPAWN_OFFSETS = new int[][] {
            {1, -1, 0},
            {0, -1, 1},
            {-1, -1, 0},
            {0, -1, -1},
            {1, -1, 1},
            {-1, -1, 1},
            {-1, -1, -1},
            {1, -1, -1},

            {1, 0, 0},
            {0, 0, 1},
            {-1, 0, 0},
            {0, 0, -1},

            {1, -2, 0},
            {0, -2, 1},
            {-1, -2, 0},
            {0, -2, -1},

            {1, 0, 1},
            {-1, 0, 1},
            {-1, 0, -1},
            {1, 0, -1},

            {1, -2, 1},
            {-1, -2, 1},
            {-1, -2, -1},
            {1, -2, -1},

            {0, 1, 0}

    };

    // configure
    public static final int[][] VEHICLE_RESPAWN_OFFSET = {
            { 2, -1, -1},
            { 2, -1,  0},
            { 2, -1,  1},

            { 1, -1,  2},
            { 0, -1,  2},
            {-1, -1,  2},

            {-2, -1,  1},
            {-2, -1,  0},
            {-2, -1, -1},

            {-1, -1, -2},
            { 0, -1, -2},
            { 1, -1,  2},


            { 2,  0, -1},
            { 2,  0,  0},
            { 2,  0,  1},

            { 1,  0,  2},
            { 0,  0,  2},
            {-1,  0,  2},

            {-2,  0,  1},
            {-2,  0,  0},
            {-2,  0, -1},

            {-1,  0, -2},
            { 0,  0, -2},
            { 1,  0,  2},


            { 2, -2, -1},
            { 2, -2,  0},
            { 2, -2,  1},

            { 1, -2,  2},
            { 0, -2,  2},
            {-1, -2,  2},

            {-2, -2,  1},
            {-2, -2,  0},
            {-2, -2, -1},

            {-1, -2, -2},
            { 0, -2, -2},
            { 1, -2,  2},

            {0, 1, 0}
    };
    final public int index;
    final public boolean active;
    final public Block unactiveCounterpart;
    public static class LazyInformation {
        public ServerWorld enderworld;
        public int dimensionScaleInverse;
        public EnderlingStructure portal;

        protected LazyInformation(ServerWorld enderworld, int dimensionScaleInverse, EnderlingStructure portal) {
            this.enderworld = enderworld;
            this.dimensionScaleInverse = dimensionScaleInverse;
            this.portal = portal;
        }
    }
    protected static @Nullable LazyInformation info;

    public static LazyInformation getInfo(MinecraftServer server) {
        if (EnderworldPortalBlock.info != null) {
            return info;
        }
        DimensionFinder enderworldFinder = new DimensionFinder(
                new Identifier(TechnoEnderling.MOD_ID, "enderworld")
        );
        enderworldFinder.lazyInit(server);
        ServerWorld enderworld = server.getWorld(enderworldFinder.get());
        assert enderworld != null;
        assert enderworld.getDimension() != null;
        int dimensionScaleInverse = (int) Math.round(1D / enderworld.getDimension().getCoordinateScale());

        // the reason this is here is, because I want, that the EnderlingStructureRegistry is ready
        Optional<EnderlingStructure> portalOptional = EnderlingStructureRegistry
                .instance()
                .get(new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal"));
        assert portalOptional.isPresent();
        EnderlingStructure portal = portalOptional.get();
        EnderworldPortalBlock.info = new LazyInformation(enderworld, dimensionScaleInverse, portal);
        return EnderworldPortalBlock.info;
    }

    public EnderworldPortalBlock(Settings settings, int index, boolean active, Block unactiveCounterpart) {
        super(settings);
        this.index = index;
        this.active = active;
        this.unactiveCounterpart = unactiveCounterpart;
        HandleLongUseServer.register(this);
    }

    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        if(player.getWorld().getBlockState(pos).getBlock() != this) {
            return;
        }
        if(!((EnderworldPortalBlock)player.getWorld().getBlockState(pos).getBlock()).active) {
            return;
        }
        if (player.getWorld().getRegistryKey().getValue().toString().equals("technoenderling:enderworld")) {
            ServerWorld destination = server.getOverworld();
            List<BlockPos> portalStream = EnderworldPortalBlock.findPortalPosToOverworld(server, pos).toList();
            TeleportParams params = EnderworldPortalBlock.getTeleportParamsWithTargetPortalPositions(server, destination, player, portalStream, pos);
            if (params == null) {
                return;
            }
            VehicleTeleport.teleportWithVehicle(params);
        } else {
            ServerWorld destination = info.enderworld;
            List<BlockPos> portalStream = EnderworldPortalBlock.findPortalPosToEnderworld(server, pos).toList();
            TeleportParams params = EnderworldPortalBlock.getTeleportParamsWithTargetPortalPositions(server, destination, player, portalStream, pos);
            if (params == null) {
                return;
            }
            VehicleTeleport.teleportWithVehicle(params);
        }
    }

    public static @Nullable TeleportParams getTeleportParamsWithTargetPortalPositions(
            MinecraftServer server,
            ServerWorld destination,
            ServerPlayerEntity player,
            List<BlockPos> portalPositions,
            BlockPos oldPortalPos
    ) {
        int[][] offsets = player.hasVehicle() ? EnderworldPortalBlock.VEHICLE_RESPAWN_OFFSET : EnderworldPortalBlock.RESPAWN_OFFSETS;
        Optional<BlockPos> chosenPortal = EnderworldPortalBlock.getValidPortal(
            portalPositions,
            server,
            destination,
            player,
            oldPortalPos.getY(),
            offsets
        );
        if (chosenPortal.isEmpty()) {
            return null;
        }
        Optional<Vec3d> destPosOptional = VehicleTeleport.findWakeUpPosition(
                player,
                destination,
                chosenPortal.get(),
                offsets,
                false
        );
        // as this was checked inside getSpawnPositionFromStream, this should never happen
        assert destPosOptional.isPresent();
        Vec3d destPos = destPosOptional.get();
        return new TeleportParams(
                player,
                destination,
                chosenPortal.get(),
                destPos.getX(),
                destPos.getY(),
                destPos.getZ()
        );
    }

    public static BlockPos enderworldToOverworld(MinecraftServer server, BlockPos posEnderworld) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        return new BlockPos(
                posEnderworld.getX() / info.dimensionScaleInverse,
                posEnderworld.getY(),
                posEnderworld.getZ() / info.dimensionScaleInverse
        );
    }
    public static Stream<BlockPos> findPortalPosToOverworld(MinecraftServer server, BlockPos posEnderworld) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        BlockPos posOverworld = EnderworldPortalBlock.enderworldToOverworld(server, posEnderworld);
        PointOfInterestStorage pointOfInterestStorage = server.getOverworld().getPointOfInterestStorage();

        // scraped and adjusted from net.minecraft.world.PortalForcer.getPortalRect
        pointOfInterestStorage.preloadChunks(server.getOverworld(), new BlockPos(posOverworld), 1);

        return pointOfInterestStorage.getInSquare(
                        ModPointsOfInterest.IS_ENDERWORLD_PORTAL,
                        new BlockPos(posOverworld),
                        info.dimensionScaleInverse,
                        PointOfInterestStorage.OccupationStatus.ANY
                )
                // I will need this when I implement the ticket propagation, therefore I will leave it there until consumed
                // this.world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(blockPos), 3, blockPos);
                .map(PointOfInterest::getPos)
                .filter(filterPos -> posOverworld.getX() == filterPos.getX())
                .filter(filterPos -> posOverworld.getZ() == filterPos.getZ());
    }
    public static BlockPos overworldToEnderworldStart(MinecraftServer server, BlockPos posOverworld) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        return new BlockPos(
                posOverworld.getX() * info.dimensionScaleInverse,
                posOverworld.getY(),
                posOverworld.getZ() * info.dimensionScaleInverse
        );
    }
    public static BlockPos overworldToEnderworldMiddle(MinecraftServer server, BlockPos posOverworld) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        return  new BlockPos(
                posOverworld.getX() * info.dimensionScaleInverse + info.dimensionScaleInverse / 2,
                posOverworld.getY(),
                posOverworld.getZ() * info.dimensionScaleInverse + info.dimensionScaleInverse / 2
        );
    }
    public static BlockPos overworldToEnderworldEnd(MinecraftServer server, BlockPos posOverworld) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        return new BlockPos(
                posOverworld.getX() * info.dimensionScaleInverse + info.dimensionScaleInverse - 1,
                posOverworld.getY(),
                posOverworld.getZ() * info.dimensionScaleInverse + info.dimensionScaleInverse - 1
        );
    }
    public static Stream<BlockPos> findPortalPosToEnderworld(MinecraftServer server, BlockPos posOverworld) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        Vec3i vecStart = EnderworldPortalBlock.overworldToEnderworldStart(server, posOverworld);
        BlockPos posMiddle = EnderworldPortalBlock.overworldToEnderworldMiddle(server, posOverworld);
        Vec3i vecEnd = EnderworldPortalBlock.overworldToEnderworldEnd(server, posOverworld);
        PointOfInterestStorage pointOfInterestStorage = info.enderworld.getPointOfInterestStorage();

        // scraped and adjusted from net.minecraft.world.PortalForcer.getPortalRect
        pointOfInterestStorage.preloadChunks(info.enderworld, posMiddle, info.dimensionScaleInverse);

        return pointOfInterestStorage.getInSquare(
                        ModPointsOfInterest.IS_ENDERWORLD_PORTAL,
                        posMiddle,
                        info.dimensionScaleInverse,
                        PointOfInterestStorage.OccupationStatus.ANY
            )
        // I will need this when I implement the ticket propagation, therefore I will leave it there until consumed
        // this.world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(blockPos), 3, blockPos);
                .map(PointOfInterest::getPos)
                .filter(filterPos -> vecStart.getX() <= filterPos.getX() && filterPos.getX() <= vecEnd.getX())
                .filter(filterPos -> vecStart.getZ() <= filterPos.getZ() && filterPos.getZ() <= vecEnd.getZ());
    }

    public static Optional<BlockPos> getValidPortal(
            List<BlockPos> possiblePortals,
            MinecraftServer server,
            ServerWorld destination,
            ServerPlayerEntity player,
            int yToFind,
            int[][] offsets
    ) {
        LazyInformation info = EnderworldPortalBlock.getInfo(server);
        WorldBorder worldBorder = destination.getWorldBorder();
        info.portal.getConvertible().lazyInit(server);
        List<Vec3i> convertibleOffsets = info.portal.getConvertible().getOffsets();
        assert convertibleOffsets != null;
        return possiblePortals.stream()
                .filter(filterPos -> info.portal
                        .getPlaceable()
                        .checkStructureOnPos(destination, filterPos, convertibleOffsets).isPresent()
                )
                .filter(worldBorder::contains)
                .sorted(new Comparator<BlockPos>() {
                    public Integer mappedValue(BlockPos pos) {
                        return Math.abs(pos.getY() - yToFind);
                    }
                    @Override
                    public int compare(BlockPos left, BlockPos right) {
                        return mappedValue(left).compareTo(mappedValue(right));
                    }
                }).filter((BlockPos filterPortalBlockedPos) -> VehicleTeleport.findWakeUpPosition(
                        player,
                        destination,
                        filterPortalBlockedPos,
                        offsets,
                        false
                ).isPresent())
                .findFirst();

    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.isClient()) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
        LazyInformation info = EnderworldPortalBlock.getInfo(world.getServer());
        if (info.portal.getPlaceable().checkStructureOnPos(
                (ServerWorld) world,
                pos,
                info.portal.getPlaceable().getAllAvailableOffsets()
                ).isEmpty()
        ) {
            return this.unactiveCounterpart.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
