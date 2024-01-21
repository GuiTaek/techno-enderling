package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.mixin.BedMixin;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.CollisionView;

import java.util.*;
import java.util.stream.Collectors;

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

    protected DimensionFinder enderworld;
    protected Double dimensionScale;

    public EnderworldPortalBlock(Settings settings, int index, boolean active) {
        super(settings);
        this.index = index;
        this.active = active;
        HandleLongUseServer.register(this);
        this.enderworld = new DimensionFinder(new Identifier("technoenderling", "enderworld"));
        this.dimensionScale = null;
    }

    protected void initWithServer(MinecraftServer server) {
        this.enderworld.lazyInit(server);
        this.initdimensionScale(server);
    }

    protected void initdimensionScale(MinecraftServer server) {
        if (this.dimensionScale == null) {
            this.dimensionScale = server.getWorld(this.enderworld.get()).getDimension().getCoordinateScale();
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
            Vec3d actualPos = player.getPos();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            Entity entity = player;
            Vec3i target = this.enderworldToOverworld(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
            Optional<Vec3d> destPosOptional = findTeleportPosToOverworld(server, player, target);
            if (destPosOptional.isEmpty()) {
                return;
            }
            Vec3d destPos = destPosOptional.get();
            // in the current world crashes
            player.teleport(destination, destPos.getX(), destPos.getY(), destPos.getZ(), yaw, pitch);
        } else {
            ServerWorld destination = server.getWorld(this.enderworld.get());
            Vec3d actualPos = player.getPos();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            Entity entity = player;
            player.teleport(destination, 0.5, 80, 0.5, yaw, pitch);
        }
    }
    protected Vec3i enderworldToOverworld(Vec3i vec) {
        Vec3d vecExact = new Vec3d(vec.getX() * this.dimensionScale, vec.getY(), vec.getZ() * this.dimensionScale);
        return new Vec3i(Math.round(vecExact.getX()), Math.round(vecExact.getY()), Math.round(vecExact.getZ()));
    }
    protected Optional<Vec3d> findTeleportPosToOverworld(MinecraftServer server, PlayerEntity player, Vec3i posOverworld) {
        this.initWithServer(server);
        //server.getWorld(this.enderworld.get())
        // there shouldn't happen an overflow
        int x = posOverworld.getX();
        int y = posOverworld.getY();
        int z = posOverworld.getZ();
        Set<BlockPos> foundPositions = new HashSet<>();
        EnderlingStructure portal = EnderlingStructureRegistry.instance().get(new Identifier("technoenderling", "enderworld_portal")).get();
        int minY = server.getOverworld().getDimension().getMinimumY();
        int maxY = server.getOverworld().getDimension().getLogicalHeight() + minY;
        List<BlockState> relevantStates = portal.getPlaceable().getUsedBlocks();
        for (int currY = minY; currY <= maxY; ++currY) {
            BlockPos pos = new BlockPos(x, currY, z);
            // for efficiency first check if there is any relevant block
            if(relevantStates.contains(server.getOverworld().getBlockState(pos))) {
                if(portal
                        .getPlaceable()
                        .checkStructureOnPos(server.getOverworld(), pos)) {
                    foundPositions.add(pos);
                }
            }
        }
        final int tempY = y;
        return foundPositions
                .stream()
                .map((BlockPos tempPos)->Math.abs(tempPos.getY() - tempY))
                .sorted()
                .findFirst()
                // todo: instead of above the portal, check where the player can spawn
                .map(
                        (Integer temp2Y) -> EnderworldPortalBlock.findWakeUpPosition(
                                player.getType(),
                                server.getOverworld(),
                                new BlockPos(x, temp2Y + tempY, z),
                                EnderworldPortalBlock.RESPAWN_OFFSETS,
                                false
                        )
                ).filter(Optional::isPresent)
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
