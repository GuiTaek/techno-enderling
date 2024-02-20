package com.gmail.guitaekm.technoenderling.networking;

import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

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
