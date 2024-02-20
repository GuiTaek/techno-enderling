package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import com.gmail.guitaekm.technoenderling.networking.TeleportDestinations;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleportScreenFactory implements ExtendedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.of("my test screen name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        TechnoEnderling.LOGGER.info("I'm going to contain some information about the portals");
        return new TeleportScreenHandler(syncId);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        // todo: save in the player
        EnderworldPortalBlock.NetherInstance source = new EnderworldPortalBlock.NetherInstance(0, "home", new BlockPos(0, 64, 0));
        List<EnderworldPortalBlock.NetherInstance> destinations = List.of(
                new EnderworldPortalBlock.NetherInstance(1, "10-64-0", new BlockPos(10, 64, 0)),
                new EnderworldPortalBlock.NetherInstance(2, "0-64-10", new BlockPos(0, 64, 10)),
                new EnderworldPortalBlock.NetherInstance(3, "10-64-10", new BlockPos(-10, 64, 10)),
                source
        );
        new TeleportDestinations(source, destinations).writeToPacket(buf);
    }
}
