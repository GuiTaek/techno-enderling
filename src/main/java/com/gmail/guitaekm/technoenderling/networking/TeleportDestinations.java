package com.gmail.guitaekm.technoenderling.networking;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TeleportDestinations {

    public final EnderworldPortalBlock.NetherInstance source;
    public final List<EnderworldPortalBlock.NetherInstance> destinations;

    public TeleportDestinations(EnderworldPortalBlock.NetherInstance source, List<EnderworldPortalBlock.NetherInstance> destinations) {
        this.source = source;
        this.destinations = destinations;
    }

    public TeleportDestinations(PacketByteBuf packet) {
        int sourceIndex = packet.readInt();
        List<EnderworldPortalBlock.NetherInstance> destinations = packet.readList(new Function<PacketByteBuf, EnderworldPortalBlock.NetherInstance>() {
            int id = 0;
            @Override
            public EnderworldPortalBlock.NetherInstance apply(PacketByteBuf packet) {
                return new EnderworldPortalBlock.NetherInstance(id++, packet.readString(), packet.readBlockPos());
            }
        });
        this.source = destinations.get(sourceIndex);
        this.destinations = destinations;
    }

    public void writeToPacket(PacketByteBuf packet) {
        // todo: could this lead to pointer problems, when
        // this.source.equals(this.destinations.get(sourceIndex))
        // but this.source != this.destinations.get(sourceIndex)?
        packet.writeInt(this.destinations.indexOf(this.source));
        packet.writeCollection(
                this.destinations,
                (packetByteBuf, netherInstance) -> {
                    packetByteBuf.writeString(netherInstance.name());
                    packetByteBuf.writeBlockPos(netherInstance.pos());
                });
    }
}
