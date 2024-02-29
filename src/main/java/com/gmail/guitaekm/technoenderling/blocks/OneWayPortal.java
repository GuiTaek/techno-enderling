package com.gmail.guitaekm.technoenderling.blocks;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.networking.HandleLongUseServer;
import com.gmail.guitaekm.technoenderling.teleport.TeleportParams;
import com.gmail.guitaekm.technoenderling.teleport.VehicleTeleport;
import com.gmail.guitaekm.technoenderling.worldgen.ModWorlds;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;

public class OneWayPortal extends Block implements HandleLongUseServer.Listener {
    public OneWayPortal(Settings settings) {
        super(settings);
    }

    public void registerServer() {
        HandleLongUseServer.register(this);
    }
    public void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(this, RenderLayer.getTranslucent());
    }

    @Override
    public void onUse(MinecraftServer server, ServerPlayerEntity player, BlockPos pos) {
        if (!this.getStateManager().getStates().contains(player.getWorld().getBlockState(pos))) {
            return;
        }
        if (!player.getWorld().getRegistryKey().equals(ModWorlds.getInfo(server).enderworldKey())) {
            TechnoEnderling.LOGGER.warn("The one way portal was used outside the enderworld. Should be impossible in survival.");
            return;
        }
        BlockPos overworldPos = EnderworldPortalBlock.enderworldToOverworld(server, pos);
        int targetHeight = server
                .getOverworld()
                .getChunk(overworldPos)
                .getHeightmap(Heightmap.Type.WORLD_SURFACE)
                .get(overworldPos.getX() & 15, overworldPos.getZ() & 15);
        VehicleTeleport.teleportWithVehicle(new TeleportParams(
                player,
                server.getOverworld(),
                overworldPos.withY(targetHeight - 1),
                overworldPos.getX() + 0.5,
                targetHeight,
                overworldPos.getZ() + 0.5
        ));
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (this.getStateManager().getStates().contains(stateFrom)) {
            return true;
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }
}