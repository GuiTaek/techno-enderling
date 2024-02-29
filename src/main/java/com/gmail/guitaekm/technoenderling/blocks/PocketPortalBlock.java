package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.access.IServerPlayerEntityAccess;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.teleport.TeleportParams;
import com.gmail.guitaekm.technoenderling.teleport.VehicleTeleport;
import com.gmail.guitaekm.technoenderling.worldgen.ModWorlds;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PocketPortalBlock extends Block implements HandleLongUseServer.Listener {
    // these will be configured through arbitrary structures -- it's just not implemented yet
    // configure
    public static Set<Vec3i> MANTLE_OFFSETS = Set.of(
            new Vec3i(0, 1, 0),
            new Vec3i(0, 2, 0),
            new Vec3i(1, 3, 0),
            new Vec3i(0, 3, 1),
            new Vec3i(0, 3, -1),
            new Vec3i(-1, 3, 0),
            new Vec3i(0, 4, 0)
    );

    // configure
    public static Vec3i CORE_OFFSET = new Vec3i(0, 3, 0);

    // configure
    public static List<Set<Block>> WARD_BLOCK = List.of(
            Set.of(
                    Blocks.END_STONE_BRICKS
            ),
            Set.of(
                    Blocks.IRON_BLOCK
            ),
            Set.of(
                    Blocks.DIAMOND_BLOCK,
                    Blocks.AMETHYST_BLOCK
            ),
            Set.of(
                    Blocks.NETHERITE_BLOCK,
                    Blocks.BEACON
            )
    );

    // configure
    public static List<Vec3i> WARD_POWERS = List.of(
            new Vec3i(4, 2, 4),
            new Vec3i(8, 4, 8),
            new Vec3i(16, 8, 16)
    );
    // configure
    public static double DROP_CHANCE = 0.8;
    public PocketPortalBlock(Settings settings) {
        super(settings);
    }
    public static void register(PocketPortalBlock block) {
        HandleLongUseServer.register(block);
    }
    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        ModWorlds.LazyInformation info = ModWorlds.getInfo(server);
        if (!player.getWorld().getBlockState(pos).getBlock().equals(this)) {
            return;
        }
        Optional<Structure> pocketPortalOptional = server
                .getStructureManager()
                .getStructure(new Identifier(TechnoEnderling.MOD_ID, "pocket_portal"));
        assert pocketPortalOptional.isPresent();
        Structure pocketPortal = pocketPortalOptional.get();
        StructureFeature<?> commonPocketPortal = Registry
                .STRUCTURE_FEATURE
                .get(new Identifier(TechnoEnderling.MOD_ID, "common_pocket_portal"));
        StructureFeature<?> rarePocketPortal = Registry
                .STRUCTURE_FEATURE
                .get(new Identifier(TechnoEnderling.MOD_ID, "rare_pocket_portal"));
        if (player.getWorld().getRegistryKey().equals(info.pocketDimensionKey())) {
            BlockPos targetPos = ((IServerPlayerEntityAccess)(Object)player).techno_enderling$getLastUsedPocketPortal();
            if (targetPos == null) {
                TechnoEnderling.LOGGER.warn("Tried to leave the pocket dimension two times in the row. The player likely used another teleportation method");
                // todo: spawn the player at spawn
                return;
            }
            ((IServerPlayerEntityAccess) player).techno_enderling$setLastUsedPocketPortal(null);
            if (!info.enderworld().getStructureAccessor().getStructureAt(targetPos, rarePocketPortal).hasChildren()) {
                if (!info.enderworld().getStructureAccessor().getStructureAt(targetPos, commonPocketPortal).hasChildren()) {
                    TechnoEnderling.LOGGER.warn("corrupted player data");
                    // todo: spawn the player at spawn
                    return;
                }
            }
            PocketPortalBlock.pocketPortalTeleport(info.enderworld(), player, pocketPortal, targetPos);

        } else if (player.getWorld().getRegistryKey().equals(info.enderworldKey())) {
            ((IServerPlayerEntityAccess) player).techno_enderling$setLastUsedPocketPortal(pos.up());
            // the position should be different for each player and depend on the seed and the hash value of the player name
            this.preparePocketDimension(info.pocketDimension(), new BlockPos(0, 0, 0));
            PocketPortalBlock.pocketPortalTeleport(info.pocketDimension(), player, pocketPortal, new BlockPos(0, 5, 0));
        } else {
            TechnoEnderling.LOGGER.warn("Player tried to walk through a pocket portal outside of enderworld and pocket dimension. Should be impossible in survival.");
        }
    }

    /**
     *
     * @param destination the destination dimension
     * @param position the bottom left corner
     * @param size the size of the pocket portal
     * @param ignoreLayers how many layers to be ignored from the bottom
     */
    public static void dropByPortalPlacement(ServerWorld destination, BlockPos position, Vec3i size, int ignoreLayers, ServerPlayerEntity player) {
        // configure
        Vec3i pocketPortalOffset = new Vec3i(-3, -5, -3);
        for (int x = 0; x < size.getX(); x++) {
            for (int y = ignoreLayers; y < size.getY(); y++) {
                for (int z = 0; z < size.getZ(); z++) {
                    Vec3i toPortalBlockOffset = new Vec3i(x, y, z).add(pocketPortalOffset).add(0, 1, 0);
                    if (MANTLE_OFFSETS.contains(toPortalBlockOffset) || CORE_OFFSET.equals(toPortalBlockOffset)) {
                        continue;
                    }
                    BlockPos toDestroy = position.add(x, y, z);
                    // somehow this needs a chunk, not the server world
                    BlockEntity blockEntity = destination.getChunk(toDestroy).getBlockEntity(toDestroy);
                    if (blockEntity instanceof Inventory inventory) {
                        for(int i = 0; i < inventory.size(); ++i) {
                            if (decideDrop(destination)) {
                                ItemScatterer.spawn(
                                        destination,
                                        toDestroy.getX(),
                                        toDestroy.getY(),
                                        toDestroy.getZ(),
                                        inventory.getStack(i)
                                );
                            }
                        }
                        Block toUpdate = destination.getBlockState(toDestroy).getBlock();
                        destination.updateComparators(toDestroy, toUpdate);
                    }
                    destination.breakBlock(toDestroy, decideDrop(destination), player);
                }
            }
        }
    }

    /**
     * asserts the portal position to be valid
     * @param destination the world to teleport to
     * @param portalPosition position of the portal block
     */
    public static void pocketPortalTeleport(ServerWorld destination, ServerPlayerEntity player, Structure pocketPortal, BlockPos portalPosition) {
        // configure
        Vec3i pocketPortalOffset = new Vec3i(-3, -5, -3);

        // configure
        Vec3i pocketPortalSize = new Vec3i(7, 10, 7);
        dropByPortalPlacement(destination, portalPosition.add(pocketPortalOffset), pocketPortalSize, 1, player);

        pocketPortal.place(
                destination,
                portalPosition.add(pocketPortalOffset),
                // the pivot parameter isn't used a single time, I looked through every implementation
                // and there is no documentation for that one
                portalPosition,
                new StructurePlacementData(),
                destination.getRandom(),
                Block.NOTIFY_ALL
        );
        // configure
        int yOff = -4;
        VehicleTeleport.teleportWithVehicle(new TeleportParams(
                player, destination, portalPosition,
                portalPosition.getX() + 0.5,
                portalPosition.getY() + 1. + yOff,
                portalPosition.getZ() + 0.5
        ));
    }
    public static boolean decideDrop(ServerWorld destination) {
        return destination.getRandom().nextDouble() <= PocketPortalBlock.DROP_CHANCE;
    }
    public void preparePocketDimension(ServerWorld pocketDimension, BlockPos pos) {
        // configure
        Vec3i portalSize = new Vec3i(7, 10, 7);
        Vec3i portalRadiusses = new Vec3i(
                Math.floorDiv(portalSize.getX() - 1, 2),
                portalSize.getY(),
                Math.floorDiv(portalSize.getZ() - 1, 2)
        );
        Vec3i radiusses = getPocketDimensionDimensions(pocketDimension, pos.add(0, 4, 0));
        // configure
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                {
                    int y = 0;
                    pocketDimension.setBlockState(pos.add(x, y, z), Blocks.BEDROCK.getDefaultState());
                }
                for (int y = 1; y <= 20; y++) {
                    if (-radiusses.getX() <= x && x <= radiusses.getX()
                            && -radiusses.getZ() <= z && z <= radiusses.getZ()
                            && y <= radiusses.getY()) {
                        continue;
                    }
                    if (-portalRadiusses.getX() <= x && x <= portalRadiusses.getX()
                        && -portalRadiusses.getZ() <= z && z <= portalRadiusses.getZ()
                            && y <= portalRadiusses.getY()
                    ) {
                        if (pocketDimension.getBlockState(pos.add(x, y, z)).getBlock().equals(Blocks.BARRIER)) {
                            // here, the pocket dimension is entered the first time
                            pocketDimension.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                        }
                        continue;
                    }
                    pocketDimension.setBlockState(pos.add(x, y, z), Blocks.BARRIER.getDefaultState());
                }
            }
        }
        for (int x = -radiusses.getX(); x <= radiusses.getX(); x++) {
            for (int z = -radiusses.getZ(); z <= radiusses.getZ(); z++) {
                for (int y = 1; y <= radiusses.getY(); y++) {
                    if (pocketDimension.getBlockState(pos.add(x, y, z)).getBlock().equals(Blocks.BARRIER)) {
                        pocketDimension.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    /**
     * temp method to check if the pocket portal is argumented
     * @param portalPos the position of the pocket portal block
     * @return the dimensions the pocket portal should have, but only the radiusses
     */
    public Vec3i getPocketDimensionDimensions(ServerWorld pocketDimension, BlockPos portalPos) {
        Vec3i firstOffset = MANTLE_OFFSETS.iterator().next();
        Block mantleBlock = pocketDimension
                .getBlockState(portalPos.add(firstOffset.getX(), firstOffset.getY(), firstOffset.getZ()))
                .getBlock();
        List<Vec3i> notFittingBlocks = MANTLE_OFFSETS.stream().filter(offset -> (
                pocketDimension
                        .getBlockState(portalPos.add(offset.getX(), offset.getY(), offset.getZ()))
                        .getBlock()
                        != mantleBlock
        )).toList();
        if (!notFittingBlocks.isEmpty()) {
            return new Vec3i(0, 0, 0);
        }
        Block coreBlock = pocketDimension.getBlockState(portalPos.add(CORE_OFFSET.getX(), CORE_OFFSET.getY(), CORE_OFFSET.getZ())).getBlock();
        for (int i = 0; i < WARD_POWERS.size(); i++) {
            if (WARD_BLOCK.get(i).contains(mantleBlock)) {
                if (WARD_BLOCK.get(i + 1).contains(coreBlock)) {
                    return WARD_POWERS.get(i);
                }
            }
        }
        return new Vec3i(0, 0, 0);
    }
}
