package com.gmail.guitaekm.endergenesis.networking;

import net.minecraft.network.PacketByteBuf;

public class RequestNetherTeleport {
    public int destinationId;
    public RequestNetherTeleport(int destinationId) {
        this.destinationId = destinationId;
    }
    public RequestNetherTeleport(PacketByteBuf packet) {
        this.destinationId = packet.readInt();
    }
    public void writeToPacket(PacketByteBuf packet) {
        packet.writeInt(this.destinationId);
    }
}
