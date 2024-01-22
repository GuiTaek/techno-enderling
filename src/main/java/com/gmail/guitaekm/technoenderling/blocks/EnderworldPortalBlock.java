package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import net.minecraft.block.Block;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.CollisionView;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class EnderworldPortalBlock extends Block implements HandleLongUseServer.Listener {
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
    final public int index;
    final public boolean active;
    protected static class LazyInformation {
        protected DimensionFinder enderworldFinder;
        protected ServerWorld enderworld;
        protected int dimensionScaleInverse;
        protected EnderlingStructure portal;

        protected LazyInformation(ServerWorld enderworld, int dimensionScaleInverse, EnderlingStructure portal) {
            this.enderworld = enderworld;
            this.dimensionScaleInverse = dimensionScaleInverse;
            this.portal = portal;
        }
    }
    protected @Nullable LazyInformation info;

    protected LazyInformation getInfo(MinecraftServer server) {
        if (this.info != null) {
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
        return new LazyInformation(enderworld, dimensionScaleInverse, portal);
    }

    public EnderworldPortalBlock(Settings settings, int index, boolean active) {
        super(settings);
        this.index = index;
        this.active = active;
        HandleLongUseServer.register(this);
    }

    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        LazyInformation info = this.getInfo(server);
        if(player.getWorld().getBlockState(pos).getBlock() != this) {
            return;
        }
        if(!((EnderworldPortalBlock)player.getWorld().getBlockState(pos).getBlock()).active) {
            return;
        }
        if (player.getWorld().getRegistryKey().getValue().toString().equals("technoenderling:enderworld")) {
            ServerWorld destination = server.getOverworld();
            Optional<Vec3d> destPosOptional = findTeleportPosToOverworld(server, player, pos);
            if (destPosOptional.isEmpty()) {
                return;
            }
            Vec3d destPos = destPosOptional.get();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            player.teleport(destination, destPos.getX(), destPos.getY(), destPos.getZ(), yaw, pitch);
        } else {
            ServerWorld destination = info.enderworld;
            Vec3d actualPos = player.getPos();
            // todo: let the player look at the portal, it shall not be
            //  easy to get the spawn point of the enderworld
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            Optional<Vec3d> destPosOptional = findTeleportPosToEnderworld(server, player, pos);
            if (destPosOptional.isEmpty()) {
                return;
            }
            Vec3d destPos = destPosOptional.get();
            player.teleport(destination, destPos.getX(), destPos.getY(), destPos.getZ(), yaw, pitch);
        }
    }
    protected Optional<Vec3d> findTeleportPosToOverworld(MinecraftServer server, PlayerEntity player, BlockPos posEnderworld) {
        LazyInformation info = this.getInfo(server);
        BlockPos posOverworld = new BlockPos(
                posEnderworld.getX() / info.dimensionScaleInverse,
                posEnderworld.getY(),
                posEnderworld.getZ() / info.dimensionScaleInverse
        );
        PointOfInterestStorage pointOfInterestStorage = server.getOverworld().getPointOfInterestStorage();

        WorldBorder worldBorder = server.getOverworld().getWorldBorder();

        // scraped and adjusted from net.minecraft.world.PortalForcer.getPortalRect
        pointOfInterestStorage.preloadChunks(server.getOverworld(), new BlockPos(posOverworld), 1);

        Stream<BlockPos> blockStream = pointOfInterestStorage.getInSquare(
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
        return this.getSpawnPositionFromStream(blockStream, server, server.getOverworld(), player, worldBorder, posOverworld.getY());
    }
    protected Optional<Vec3d> findTeleportPosToEnderworld(MinecraftServer server, PlayerEntity player, BlockPos posOverworld) {
        LazyInformation info = this.getInfo(server);
        Vec3i vecStart = new Vec3i(
                posOverworld.getX() * info.dimensionScaleInverse,
                posOverworld.getY(),
                posOverworld.getZ() * info.dimensionScaleInverse
        );
        BlockPos posMiddle = new BlockPos(
                vecStart.getX() + info.dimensionScaleInverse / 2,
                vecStart.getY(),
                vecStart.getZ() + info.dimensionScaleInverse / 2
        );
        Vec3i vecEnd = new Vec3i(
                vecStart.getX() + info.dimensionScaleInverse - 1,
                vecStart.getY(),
                vecStart.getZ() + info.dimensionScaleInverse - 1
        );
        PointOfInterestStorage pointOfInterestStorage = info.enderworld.getPointOfInterestStorage();

        WorldBorder worldBorder = info.enderworld.getWorldBorder();

        // scraped and adjusted from net.minecraft.world.PortalForcer.getPortalRect
        pointOfInterestStorage.preloadChunks(info.enderworld, posMiddle, info.dimensionScaleInverse);

        Stream<BlockPos> blockStream = pointOfInterestStorage.getInSquare(
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
        return this.getSpawnPositionFromStream(blockStream, server, info.enderworld, player, worldBorder, posMiddle.getY());
    }

    protected Optional<Vec3d> getSpawnPositionFromStream(
            Stream<BlockPos> stream,
            MinecraftServer server,
            ServerWorld destination,
            PlayerEntity player,
            WorldBorder worldBorder,
            int yToFind
    ) {
        LazyInformation info = this.getInfo(server);
        return stream
                .filter(filterPos -> info.portal
                        .getPlaceable()
                        .checkStructureOnPos(destination, filterPos)
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
                })
                .map((BlockPos filterPortalBlockedPos) -> EnderworldPortalBlock.findWakeUpPosition(
                        player.getType(),
                        destination,
                        filterPortalBlockedPos,
                        EnderworldPortalBlock.RESPAWN_OFFSETS,
                        false
                )).findFirst()
                .filter(Optional::isPresent)
                .map(Optional::get);

    }
    // scraped from net.minecraft.block.BedBlock, as I need changes that aren't in the BedBlock
    protected static Optional<Vec3d> findWakeUpPosition(EntityType<?> type, CollisionView world, BlockPos pos, int[][] possibleOffsets, boolean ignoreInvalidPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int[] is : possibleOffsets) {
            mutable.set(pos.getX() + is[0], pos.getY() + is[1], pos.getZ() + is[2]);
            Vec3d vec3d = Dismounting.findRespawnPos(type, world, mutable, ignoreInvalidPos);
            if (vec3d == null) continue;
            return Optional.of(vec3d);
        }
        return Optional.empty();
    }
}
