package com.gmail.guitaekm.technoenderling.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeMixin {
    @Invoker("register")
    public static PointOfInterestType register(String id, Set<BlockState> workStationStates, int ticketCount, int searchDistance) {
        return null;
    }

    @Invoker("getAllStatesOf")
    public static Set<BlockState> getAllStatesOf(Block block) {
        return null;
    }
}
