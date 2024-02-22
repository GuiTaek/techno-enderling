package com.gmail.guitaekm.technoenderling.access;

import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IServerPlayerNetherEnderworldPortal {
    void techno_nederling$remove(BlockPos pos);
    void techno_enderling$remove(EnderworldPortalBlock.NetherInstance portal);
    void techno_enderling$add(EnderworldPortalBlock.NetherInstance portal);
    List<EnderworldPortalBlock.NetherInstance> techno_enderling$getDestinations();
    void techno_enderling$setSource(@Nullable EnderworldPortalBlock.NetherInstance source);
    EnderworldPortalBlock.NetherInstance techno_enderling$addIfNotPresent(BlockPos pos);
    @Nullable EnderworldPortalBlock.NetherInstance techno_enderling$getSource();

}
