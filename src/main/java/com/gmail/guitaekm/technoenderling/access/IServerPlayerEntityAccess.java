package com.gmail.guitaekm.technoenderling.access;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IServerPlayerEntityAccess {
    void technoEnderling$setLastUsedPocketPortal(@Nullable BlockPos position);
    @Nullable BlockPos technoEnderling$getLastUsedPocketPortal();
}
