package com.gmail.guitaekm.endergenesis.mixin;

import com.gmail.guitaekm.endergenesis.access.IChunkTicketManagerAccess;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.util.collection.SortedArraySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkTicketManager.class)
public abstract class ChunkTicketManagerMixin implements IChunkTicketManagerAccess {
    @Shadow protected abstract SortedArraySet<ChunkTicket<?>> getTicketSet(long position);

    public SortedArraySet<ChunkTicket<?>> techno_enderling$getTicketSetPublic(long position) {
        return this.getTicketSet(position);
    }
}
