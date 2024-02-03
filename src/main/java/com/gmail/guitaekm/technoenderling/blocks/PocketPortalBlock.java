package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.access.IServerPlayerEntityAccess;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.utils.DimensionFinder;
import com.gmail.guitaekm.technoenderling.utils.TeleportParams;
import com.gmail.guitaekm.technoenderling.utils.VehicleTeleport;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PocketPortalBlock extends Block implements HandleLongUseServer.Listener {
    public PocketPortalBlock(Settings settings) {
        super(settings);
    }
    public static void register(PocketPortalBlock block) {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> PocketPortalBlock.info = null);
        HandleLongUseServer.register(block);
    }
    public static class LazyInformation {
        public ServerWorld pocketDimension;
        public RegistryKey<World> pocketDimensionKey;
        public ServerWorld enderworld;
        public RegistryKey<World> enderworldKey;
        public LazyInformation(MinecraftServer server) {
            {
                DimensionFinder finderPocket = new DimensionFinder(new Identifier(TechnoEnderling.MOD_ID, "pocket_dimension"));
                finderPocket.lazyInit(server);
                this.pocketDimensionKey = finderPocket.get();
                this.pocketDimension = server.getWorld(finderPocket.get());
            }
            {
                DimensionFinder finderEnderworld = new DimensionFinder(new Identifier(TechnoEnderling.MOD_ID, "enderworld"));
                finderEnderworld.lazyInit(server);
                this.enderworldKey = finderEnderworld.get();
                this.enderworld = server.getWorld(finderEnderworld.get());
            }
        }
    }
    protected static @Nullable LazyInformation info = null;
    public static LazyInformation getInfo(MinecraftServer server) {
        if (PocketPortalBlock.info == null) {
            PocketPortalBlock.info = new LazyInformation(server);
        }
        return PocketPortalBlock.info;
    }
    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        if (!player.getWorld().getBlockState(pos).getBlock().equals(this)) {
            return;
        }
        LazyInformation info = PocketPortalBlock.getInfo(server);
        if (player.getWorld().getRegistryKey().equals(info.pocketDimensionKey)) {
            BlockPos targetPos = ((IServerPlayerEntityAccess)(Object)player).technoEnderling$getLastUsedPocketPortal();
            if (targetPos == null) {
                TechnoEnderling.LOGGER.warn("Tried to leave the pocket dimension two times in the row. The player likely used another teleportation method");
                return;
            }
            ((IServerPlayerEntityAccess) player).technoEnderling$setLastUsedPocketPortal(null);
            VehicleTeleport.teleportWithVehicle(new TeleportParams(
                    player, info.enderworld, targetPos,
                    targetPos.getX() + 0.5,
                    targetPos.getY() + 1.,
                    targetPos.getZ() + 0.5
            ));
        } else if (player.getWorld().getRegistryKey().equals(info.enderworldKey)) {
            ((IServerPlayerEntityAccess) player).technoEnderling$setLastUsedPocketPortal(pos.up());
            this.preparePocketDimension(info.pocketDimension, new BlockPos(0, 0, 0));
            VehicleTeleport.teleportWithVehicle(new TeleportParams(
                    player, info.pocketDimension, new BlockPos(0, 0, 0),
                    0.5, 1, 0.5
            ));
        } else {
            TechnoEnderling.LOGGER.warn("Player tried to walk through a pocket portal outside of enderworld and pocket dimension. Should be impossible in survival.");
        }
    }
    public void preparePocketDimension(ServerWorld pocketDimension, BlockPos pos) {
        // configure
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                {
                    int y = 0;
                    pocketDimension.setBlockState(pos.add(x, y, z), Blocks.BEDROCK.getDefaultState());
                }
                for (int y = 1; y <= 20; y++) {
                    if (-10 <= x && x <= 10
                            && -10 <= z && z <= 10
                            && y <= 10) {
                        continue;
                    }
                    pocketDimension.setBlockState(pos.add(x, y, z), Blocks.BARRIER.getDefaultState());
                }
            }
        }
        //configure
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                for (int y = 1; y <= 10; y++) {
                    if (pocketDimension.getBlockState(pos.add(x, y, z)).getBlock().equals(Blocks.BARRIER)) {
                        pocketDimension.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        // configure
        for (int x = -5; x <= 5; x++) {
            for (int y = 1; y <= 3; y++) {
                for (int z = -5; z <= 5; z++) {
                    pocketDimension.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                }
            }
        }
        // replace with structure
        pocketDimension.setBlockState(pos, ModBlocks.POCKET_PORTAL_BLOCK.getDefaultState());
    }
}
