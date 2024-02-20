// scraped from
// https://github.com/Ladysnake/Requiem/blob/1.19/src/main/java/ladysnake/requiem/common/screen/RiftScreenHandler.java
package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import com.gmail.guitaekm.technoenderling.networking.TeleportDestinations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TeleportScreenHandler extends ScreenHandler {
    public TeleportScreenHandler(
            int syncId
    ) {
        super(RegisterGui.TELEPORT_SCREEN_HANDLER, syncId);
        this.source = null;
        this.registeredEnderworldPortalPositions = null;
    }
    public TeleportScreenHandler(
            int syncId, PlayerInventory inventory, PacketByteBuf buf
    ) {
        super(RegisterGui.TELEPORT_SCREEN_HANDLER, syncId);
        TeleportDestinations packet = new TeleportDestinations(buf);
        this.source = packet.source;
        this.registeredEnderworldPortalPositions = packet.destinations;
    }

    public EnderworldPortalBlock.NetherInstance source;
    public List<EnderworldPortalBlock.NetherInstance> registeredEnderworldPortalPositions;

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
