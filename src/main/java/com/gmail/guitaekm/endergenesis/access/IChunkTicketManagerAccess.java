package com.gmail.guitaekm.endergenesis.access;

import net.minecraft.server.world.ChunkTicket;
import net.minecraft.util.collection.SortedArraySet;

public interface IChunkTicketManagerAccess {
    public SortedArraySet<ChunkTicket<?>> endergenesis$getTicketSetPublic(long position);
}
