package com.gmail.guitaekm.technoenderling.access;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IServerPlayerEntityAccess {
    void techno_enderling$setLastUsedPocketPortal(@Nullable BlockPos position);
    @Nullable BlockPos techno_enderling$getLastUsedPocketPortal();
}
