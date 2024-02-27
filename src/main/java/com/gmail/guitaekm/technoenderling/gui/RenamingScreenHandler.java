package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.access.IServerPlayerNetherEnderworldPortal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class RenamingScreenHandler extends ScreenHandler {
    public String currName;
    public BlockPos pos;

    // server
    public RenamingScreenHandler(int syncId, String name, BlockPos pos) {
        super(RegisterGui.RENAMING_SCREEN_SCREEN_HANDLER_TYPE, syncId);
        this.currName = name;
        this.pos = pos;
    }

    // client
    public RenamingScreenHandler(
            int syncId, PlayerInventory inventory, PacketByteBuf buf
    ) {
        super(RegisterGui.RENAMING_SCREEN_SCREEN_HANDLER_TYPE, syncId);
        this.currName = buf.readString();
        this.pos = buf.readBlockPos();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.squaredDistanceTo(
                (double) pos.getX() + 0.5,
                (double) pos.getY() + 0.5,
                (double) pos.getZ() + 0.5
        ) <= 64;
    }
}
