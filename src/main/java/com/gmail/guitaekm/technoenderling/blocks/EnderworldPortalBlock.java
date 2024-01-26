package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.CollisionView;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
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
            { 1, -2,  2}
    };
    final public int index;
    final public boolean active;
    protected static class LazyInformation {
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
            List<BlockPos> portalStream = this.findPortalPosToOverworld(server, pos).toList();
            this.teleportWithTargetPortalPositions(server, destination, player, portalStream, pos);
        } else {
            ServerWorld destination = info.enderworld;
            List<BlockPos> portalStream = this.findPortalPosToEnderworld(server, pos).toList();
            this.teleportWithTargetPortalPositions(server, destination, player, portalStream, pos);

        }
    }

    protected void teleportWithTargetPortalPositions(
            MinecraftServer server,
            ServerWorld destination,
            ServerPlayerEntity player,
            List<BlockPos> portalPositions,
            BlockPos oldPortalPos
    ) {
        int[][] offsets = player.hasVehicle() ? EnderworldPortalBlock.VEHICLE_RESPAWN_OFFSET : EnderworldPortalBlock.RESPAWN_OFFSETS;
        Optional<BlockPos> chosenPortal = this.getValidPortal(
            portalPositions,
            server,
            destination,
            player,
            oldPortalPos.getY(),
            offsets
        );
        if (chosenPortal.isEmpty()) {
            return;
        }
        Optional<Vec3d> destPosOptional = EnderworldPortalBlock.findWakeUpPosition(
                player,
                destination,
                chosenPortal.get(),
                offsets,
                false
        );
        // as this was checked inside getSpawnPositionFromStream, this should never happen
        assert destPosOptional.isPresent();
        Vec3d destPos = destPosOptional.get();
        EnderworldPortalBlock.teleportWithVehicle(player, destination, chosenPortal.get(), destPos.getX(), destPos.getY(), destPos.getZ());
    }

    protected static void teleportWithVehicle(ServerPlayerEntity player, ServerWorld targetWorld, BlockPos portalPos, double x, double y, double z) {
        if(player.hasVehicle()) {
            VehicleRecursionStructure<Entity> vehicleRecursionStructure = VehicleRecursionStructure.parseVertex(
                    player.getRootVehicle(),
                    Entity::getPassengerList,
                    entity -> {
                        if(entity.hasVehicle()) {
                            entity.dismountVehicle();
                        }
                        return EnderworldPortalBlock.teleportUnmountedEntity(entity, targetWorld, portalPos, x, y, z);
                    });
            vehicleRecursionStructure.depthFirstSearch((parent, child) -> child.startRiding(parent));
            return;
        }
        EnderworldPortalBlock.teleportUnmountedEntity(player, targetWorld, portalPos, x, y, z);
    }
    protected static float getYawDirection(double x, double z, BlockPos portalPos) {
        // scraped and modified from DrownedEntity.tick
        // weirdly enough this isn't part of a method in a central place
        int dx = (int) (portalPos.getX() - Math.floor(x));
        int dz = (int) (portalPos.getZ() - Math.floor(z));
        // by the way, the magic number 57.29... is just 180/pi, and it actually makes sense to
        // have it hard coded I guess for performance
        return (float)(MathHelper.atan2(dz, dx) * 57.2957763671875) - 90;
    }
    protected static Entity teleportUnmountedEntity(Entity entity, ServerWorld targetWorld, BlockPos portalPos, double x, double y, double z) {
        float yaw = EnderworldPortalBlock.getYawDirection(x, z, portalPos);

        if (entity instanceof ServerPlayerEntity player) {
            player.teleport(targetWorld, x, y, z, yaw, +0);
            return player;
        }
        // scraped from the needed part of net.minecraft.server.command.TeleportCommand.teleport
        // again, I don't want to change this part, I just want to use it
        Entity oldEntity = entity;
        oldEntity.detach();
        entity = entity.getType().create(targetWorld);
        assert entity != null;

        entity.copyFrom(oldEntity);
        entity.refreshPositionAndAngles(x, y, z, yaw, 0);
        entity.setHeadYaw(yaw);
        entity.setBodyYaw(entity.getYaw());
        oldEntity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
        targetWorld.onDimensionChanged(entity);

        entity.setYaw(EnderworldPortalBlock.getYawDirection(x, z, portalPos));
        entity.setBodyYaw(entity.getYaw());
        entity.setPitch(0);
        return entity;
    }
    protected Stream<BlockPos> findPortalPosToOverworld(MinecraftServer server, BlockPos posEnderworld) {
        LazyInformation info = this.getInfo(server);
        BlockPos posOverworld = new BlockPos(
                posEnderworld.getX() / info.dimensionScaleInverse,
                posEnderworld.getY(),
                posEnderworld.getZ() / info.dimensionScaleInverse
        );
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
    protected Stream<BlockPos> findPortalPosToEnderworld(MinecraftServer server, BlockPos posOverworld) {
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

    protected static List<Entity> getRidingStack(ServerPlayerEntity player) {
        Stack<Entity> toCheck = new Stack<>();
        List<Entity> result = new ArrayList<>();
        toCheck.add(player.getRootVehicle());
        while (!toCheck.isEmpty()) {
            Entity currEntity = toCheck.pop();
            toCheck.addAll(currEntity.getPassengerList());
            result.add(currEntity);
        }
        return new ArrayList<>(result);
    }

    protected Optional<BlockPos> getValidPortal(
            List<BlockPos> possiblePortals,
            MinecraftServer server,
            ServerWorld destination,
            ServerPlayerEntity player,
            int yToFind,
            int[][] offsets
    ) {
        LazyInformation info = this.getInfo(server);
        WorldBorder worldBorder = destination.getWorldBorder();
        return possiblePortals.stream()
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
                }).filter((BlockPos filterPortalBlockedPos) -> EnderworldPortalBlock.findWakeUpPosition(
                        player,
                        destination,
                        filterPortalBlockedPos,
                        offsets,
                        false
                ).isPresent())
                .findFirst();

    }
    // scraped from net.minecraft.block.BedBlock, as I need changes that aren't in the BedBlock
    protected static Optional<Vec3d> findWakeUpPosition(ServerPlayerEntity player, CollisionView world, BlockPos pos, int[][] possibleOffsets, boolean ignoreInvalidPos) {
        Vec3d toCheck;
        for (int[] is : possibleOffsets) {
            toCheck = new Vec3d(pos.getX() + is[0] + 0.5, pos.getY() + is[1], pos.getZ() + is[2] + 0.5);
            if (!enoughSpaceForEntities(world, getRidingStack(player), toCheck)) {
                continue;
            }
            return Optional.of(toCheck);
        }
        return Optional.empty();
    }

    public static boolean enoughSpaceForEntities(CollisionView world, List<Entity> entities, Vec3d pos) {
        for (Entity entity : entities) {
            Vec3d feet = entity
                    .getBoundingBox()
                    .getCenter()
                    .subtract(0, entity.getBoundingBox().getYLength() / 2, 0);
            Box boxAtPos = entity
                    .getBoundingBox()
                    .offset(feet.multiply(-1))
                    .offset(pos);
            if (!world.isSpaceEmpty(boxAtPos)) {
                return false;
            }
        }
        return true;
    }
}
