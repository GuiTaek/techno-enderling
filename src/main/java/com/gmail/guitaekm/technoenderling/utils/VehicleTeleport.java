package com.gmail.guitaekm.technoenderling.utils;

import com.gmail.guitaekm.technoenderling.blocks.TreeTraverser;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class VehicleTeleport {
    public static void teleportWithVehicle(ServerPlayerEntity player, ServerWorld targetWorld, BlockPos portalPos, double x, double y, double z) {
        if(player.hasVehicle()) {
            TreeTraverser<Entity> treeTraverser = TreeTraverser.parseVertex(
                    player.getRootVehicle(),
                    Entity::getPassengerList,
                    entity -> {
                        if(entity.hasVehicle()) {
                            entity.dismountVehicle();
                        }
                        return teleportUnmountedEntity(entity, targetWorld, portalPos, x, y, z);
                    });
            treeTraverser.depthFirstSearch((parent, child) -> child.startRiding(parent));
            return;
        }
        teleportUnmountedEntity(player, targetWorld, portalPos, x, y, z);
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
    public static Optional<Vec3d> findWakeUpPosition(ServerPlayerEntity player, CollisionView world, BlockPos pos, int[][] possibleOffsets, boolean ignoreInvalidPos) {
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
