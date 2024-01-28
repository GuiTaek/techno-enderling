package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class OnStructureActivate {
    public interface Listener {
        ActionResult onStructureActivate(ServerPlayerEntity player, ServerWorld world, EnderlingStructure structure, BlockPos root);
    }
    final protected Set<Listener> listeners;

    protected OnStructureActivate() {
        this.listeners = new HashSet<>();
    }

    private static OnStructureActivate instance = new OnStructureActivate();

    static public OnStructureActivate getInstance() {
        return instance;
    }

    public void register(Listener listener) {
        this.listeners.add(listener);
    }

    public void deregister(Listener listener) {
        this.listeners.remove(listener);
    }

    public boolean callListeners(ServerPlayerEntity player, ServerWorld world, EnderlingStructure structure, BlockPos root) {
        for (Listener listener : this.listeners) {
            ActionResult result = listener.onStructureActivate(player, world, structure, root);
            if (result.isAccepted()) {
                return true;
            } else if (result != ActionResult.PASS) {
                return false;
            }
        }
        // when there is no listener, it shall just work
        return true;
    }
}
