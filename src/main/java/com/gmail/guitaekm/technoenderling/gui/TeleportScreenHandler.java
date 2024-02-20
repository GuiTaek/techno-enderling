// scraped from
// https://github.com/Ladysnake/Requiem/blob/1.19/src/main/java/ladysnake/requiem/common/screen/RiftScreenHandler.java
package com.gmail.guitaekm.technoenderling.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TeleportScreenHandler extends ScreenHandler {
    public TeleportScreenHandler(
            BlockPos source,
            List<BlockPos> registeredEnderworldPortalPositions,
            int syncId
    ) {
        super(RegisterGui.TELEPORT_SCREEN_HANDLER, syncId);
        this.source = source;
        this.registeredEnderworldPortalPositions = registeredEnderworldPortalPositions;
    }

    public BlockPos source;
    public List<BlockPos> registeredEnderworldPortalPositions;

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
