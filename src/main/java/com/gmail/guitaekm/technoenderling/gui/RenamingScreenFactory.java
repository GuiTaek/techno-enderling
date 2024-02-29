package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.access.IServerPlayerNetherEnderworldPortal;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class RenamingScreenFactory implements ExtendedScreenHandlerFactory {
    public String name;
    public BlockPos pos;
    public RenamingScreenFactory(String name, BlockPos pos) {
        this.name = name;
        this.pos = pos;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RenamingScreenHandler(syncId, name, pos, (ServerPlayerEntity) player);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(this.name);
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Rename Portal");
    }
}