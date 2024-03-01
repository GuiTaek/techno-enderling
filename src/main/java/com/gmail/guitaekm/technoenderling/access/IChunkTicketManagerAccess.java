package com.gmail.guitaekm.technoenderling.access;

import net.minecraft.server.world.ChunkTicket;
import net.minecraft.util.collection.SortedArraySet;

public interface IChunkTicketManagerAccess {
    public SortedArraySet<ChunkTicket<?>> techno_enderling$getTicketSetPublic(long position);
}
