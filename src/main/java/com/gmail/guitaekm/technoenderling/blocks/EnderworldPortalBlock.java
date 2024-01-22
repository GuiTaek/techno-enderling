package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
import net.minecraft.world.CollisionView;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.*;
import java.util.function.Predicate;
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

    protected DimensionFinder enderworldFinder;
    protected ServerWorld enderworld;
    protected Integer dimensionScaleInverse;
    protected EnderlingStructure portal;

    public EnderworldPortalBlock(Settings settings, int index, boolean active) {
        super(settings);
        this.index = index;
        this.active = active;
        HandleLongUseServer.register(this);
        this.enderworldFinder = new DimensionFinder(new Identifier("technoenderling", "enderworld"));
        this.dimensionScaleInverse = null;
        this.portal = null;
    }

    protected void initWithServer(MinecraftServer server) {
        this.enderworldFinder.lazyInit(server);
        this.initEnderworld(server);
        this.initDimensionScale(server);
        // the reason this is here is, because I want, that the EnderlingStructureRegistry is ready
        this.portal = EnderlingStructureRegistry.instance().get(new Identifier("technoenderling", "enderworld_portal")).get();
    }

    protected void initEnderworld(MinecraftServer server) {
        this.enderworldFinder.lazyInit(server);
        if (this.enderworld == null) {
            this.enderworld = server.getWorld(this.enderworldFinder.get());
        }
    }

    protected void initDimensionScale(MinecraftServer server) {
        this.initEnderworld(server);
        if (this.dimensionScaleInverse == null) {
            this.dimensionScaleInverse = (int) Math.round(1D / this.enderworld.getDimension().getCoordinateScale());
        }
    }

    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        this.initWithServer(server);
        if(player.getWorld().getBlockState(pos).getBlock() != this) {
            return;
        }
        if (player.getWorld().getRegistryKey().getValue().toString().equals("technoenderling:enderworld")) {
            ServerWorld destination = server.getOverworld();
            Vec3i target = this.enderworldToOverworld(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
            Optional<Vec3d> destPosOptional = findTeleportPosToOverworld(server, player, target);
            if (destPosOptional.isEmpty()) {
                return;
            }
            Vec3d destPos = destPosOptional.get();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            player.teleport(destination, destPos.getX(), destPos.getY(), destPos.getZ(), yaw, pitch);
        } else {
            ServerWorld destination = this.enderworld;
            Vec3d actualPos = player.getPos();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            Pair<Vec3i, Vec3i> target = this.overworldToEnderworld(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
            Optional<Vec3d> destPosOptional = findTeleportPosToEnderworld(server, player, target);
            if (destPosOptional.isEmpty()) {
                return;
            }
            Vec3d destPos = destPosOptional.get();
            player.teleport(destination, destPos.getX(), destPos.getY(), destPos.getZ(), yaw, pitch);
        }
    }
    protected Vec3i enderworldToOverworld(Vec3i vec) {
        Vec3d vecExact = new Vec3d(vec.getX() / this.dimensionScaleInverse, vec.getY(), vec.getZ() / this.dimensionScaleInverse);
        return new Vec3i(Math.floor(vecExact.getX()), Math.floor(vecExact.getY()), Math.floor(vecExact.getZ()));
    }
    protected Pair<Vec3i, Vec3i> overworldToEnderworld(Vec3i vec) {
        Vec3i vecStart = new Vec3i(
                vec.getX() * this.dimensionScaleInverse,
                vec.getY(),
                vec.getZ() * this.dimensionScaleInverse
        );
        Vec3i vecEnd = new Vec3i(
                vecStart.getX() + this.dimensionScaleInverse - 1,
                vec.getY(),
                vecStart.getZ() + this.dimensionScaleInverse - 1
        );
        return new Pair<>(vecStart, vecEnd);
    }
    protected Optional<Vec3d> findTeleportPosToOverworld(MinecraftServer server, PlayerEntity player, Vec3i posOverworld) {
        this.initWithServer(server);
        PointOfInterestStorage pointOfInterestStorage = server.getOverworld().getPointOfInterestStorage();

        WorldBorder worldBorder = server.getOverworld().getWorldBorder();

        // scraped and adjusted from net.minecraft.world.PortalForcer.getPortalRect
        pointOfInterestStorage.preloadChunks(server.getOverworld(), new BlockPos(posOverworld), 1);

        Stream<BlockPos> blockStream = pointOfInterestStorage.getInSquare(
                        ModPointsOfInterest.IS_ENDERWORLD_PORTAL,
                        new BlockPos(posOverworld),
                        this.dimensionScaleInverse,
                        PointOfInterestStorage.OccupationStatus.ANY
                )
                // I will need this when I implement the ticket propagation, therefore I will leave it there until consumed
                // this.world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(blockPos), 3, blockPos);
                .map(PointOfInterest::getPos)
                .filter(filterPos -> posOverworld.getX() == filterPos.getX())
                .filter(filterPos -> posOverworld.getZ() == filterPos.getZ());
        return this.getSpawnPositionFromStream(blockStream, server.getOverworld(), player, worldBorder, posOverworld.getY());
    }
    protected Optional<Vec3d> findTeleportPosToEnderworld(MinecraftServer server, PlayerEntity player, Pair<Vec3i, Vec3i> enderworldRange) {
        this.initWithServer(server);
        assert enderworldRange.getLeft().getY() == enderworldRange.getRight().getY();
        PointOfInterestStorage pointOfInterestStorage = this.enderworld.getPointOfInterestStorage();

        Vec3i doublePos = enderworldRange.getLeft().add(enderworldRange.getRight());
        Vec3d posDouble = new Vec3d(doublePos.getX(), doublePos.getY(), doublePos.getZ()).multiply(0.5);
        BlockPos pos = new BlockPos(Math.round(posDouble.getX()), Math.round(posDouble.getY()), Math.round(posDouble.getZ()));

        WorldBorder worldBorder = this.enderworld.getWorldBorder();

        // scraped and adjusted from net.minecraft.world.PortalForcer.getPortalRect
        pointOfInterestStorage.preloadChunks(this.enderworld, pos, this.dimensionScaleInverse);

        Stream<BlockPos> blockStream = pointOfInterestStorage.getInSquare(
                        ModPointsOfInterest.IS_ENDERWORLD_PORTAL,
                        pos,
                        this.dimensionScaleInverse,
                        PointOfInterestStorage.OccupationStatus.ANY
            )
        // I will need this when I implement the ticket propagation, therefore I will leave it there until consumed
        // this.world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(blockPos), 3, blockPos);
                .map(PointOfInterest::getPos)
                .filter(filterPos -> enderworldRange.getLeft().getX() <= filterPos.getX() && filterPos.getX() <= enderworldRange.getRight().getX())
                .filter(filterPos -> enderworldRange.getLeft().getZ() <= filterPos.getZ() && filterPos.getZ() <= enderworldRange.getRight().getZ());
        return this.getSpawnPositionFromStream(blockStream, this.enderworld, player, worldBorder, pos.getY());
    }

    protected Optional<Vec3d> getSpawnPositionFromStream(
            Stream<BlockPos> stream,
            ServerWorld destination,
            PlayerEntity player,
            WorldBorder worldBorder,
            int yToFind
    ) {
        return stream
                .filter(filterPos -> this.portal
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
