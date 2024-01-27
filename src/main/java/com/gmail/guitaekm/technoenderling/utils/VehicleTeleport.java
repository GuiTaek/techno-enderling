package com.gmail.guitaekm.technoenderling.utils;

import com.gmail.guitaekm.technoenderling.blocks.TreeTraverser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.SpawnHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class VehicleTeleport {
    protected static int[][] VEHICLE_SPAWN_CHECK_OFFSET = {
            {0, 0, 0},
            {0, 0, 1},
            {1, 0, 1},
            {1, 0, 0},
            {1, 0, -1},
            {0, 0, -1},
            {-1, 0, -1},
            {-1, 0, 0},
            {-1, 0, 1}
    };
    public static void teleportWithVehicle(TeleportParams params) {
        TreeTraverser<Entity> treeTraverser = TreeTraverser.parseVertex(
                params.player.getRootVehicle(),
                Entity::getPassengerList,
                entity -> {
                    if(entity.hasVehicle()) {
                        entity.dismountVehicle();
                    }
                    return teleportUnmountedEntity(entity, params.targetWorld, params.portalPos, params.x, params.y, params.z);
                });
        // tps with /tp of boats with players inside boats are completely ignored, players gets dismounted on tps
        // this all happens on client leading to desync randomly and unpredictable
        // having this line is more convenient, unfortuntely, you would get teleported far away from your
        // portal I tried tinkering this, nothing what I tried worked consistantly
        // thinking of custom vehicles like e.g. create I think we're all better not supporting staying in the
        // vehicle on teleport
        //treeTraverser.depthFirstSearch((parent, child) -> child.startRiding(parent, true));
    }

    public static float getYawDirection(double x, double z, BlockPos portalPos) {
        // scraped and modified from DrownedEntity.tick
        // weirdly enough this isn't part of a method in a central place
        int dx = (int) (portalPos.getX() - Math.floor(x));
        int dz = (int) (portalPos.getZ() - Math.floor(z));
        // by the way, the magic number 57.29... is just 180/pi, and it actually makes sense to
        // have it hard coded I guess for performance
        return (float)(MathHelper.atan2(dz, dx) * 57.2957763671875) - 90;
    }

    protected static Entity teleportUnmountedEntity(Entity entity, ServerWorld targetWorld, BlockPos portalPos, double x, double y, double z) {
        float yaw = getYawDirection(x, z, portalPos);

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

        return entity;
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

    // scraped from net.minecraft.block.BedBlock, as I need changes that aren't in the BedBlock
    public static Optional<Vec3d> findWakeUpPosition(ServerPlayerEntity player, ServerWorld world, BlockPos pos, int[][] possibleOffsets, boolean ignoreInvalidPos) {
        BlockPos toCheck;
        for (int[] is : possibleOffsets) {
            toCheck = new BlockPos(pos.getX() + is[0], pos.getY() + is[1], pos.getZ() + is[2]);
            if (player.hasVehicle() && !VehicleTeleport.canVehicleSpawn(world, toCheck)) continue;
            if (!canSpawnAllEntities(world, getRidingStack(player), toCheck)) continue;
            Vec3d result = new Vec3d(toCheck.getX() + 0.5, toCheck.getY(), toCheck.getZ() + 0.5);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public static boolean canSpawnAllEntities(ServerWorld world, List<Entity> entities, BlockPos pos) {
        for (Entity entity : entities) {
            if (!SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, pos, entity.getType())) {
                return false;
            }
        }
        return true;
    }
    public static boolean canVehicleSpawn(ServerWorld world, BlockPos pos) {
        for (int[] offset : VehicleTeleport.VEHICLE_SPAWN_CHECK_OFFSET) {
            if(!world.getBlockState(pos.add(offset[0], offset[1], offset[2])).getCollisionShape(world, pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
