package com.gmail.guitaekm.endergenesis.access;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IServerPlayerEntityAccess {
    void endergenesis$setLastUsedPocketPortal(@Nullable BlockPos position);
    @Nullable BlockPos endergenesis$getLastUsedPocketPortal();
}
