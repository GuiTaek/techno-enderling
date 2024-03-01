package com.gmail.guitaekm.endergenesis.access;

import com.gmail.guitaekm.endergenesis.blocks.EnderworldPortalBlock;
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
    void techno_enderling$setName(String currName, String newName);
    void techno_enderling$removeWithName(String name);

}
