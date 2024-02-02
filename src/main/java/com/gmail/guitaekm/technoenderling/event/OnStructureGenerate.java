package com.gmail.guitaekm.technoenderling.event;

import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Set;

public class OnStructureGenerate {
    public interface Listener {
        ActionResult onStructureGenerate(WorldAccess world, EnderlingStructure structure, BlockPos root);
    }
    final protected Set<OnStructureGenerate.Listener> listeners;

    protected OnStructureGenerate() {
        this.listeners = new HashSet<>();
    }

    final private static OnStructureGenerate instance = new OnStructureGenerate();

    static public OnStructureGenerate getInstance() {
        return instance;
    }

    public void register(OnStructureGenerate.Listener listener) {
        this.listeners.add(listener);
    }

    public void deregister(OnStructureGenerate.Listener listener) {
        this.listeners.remove(listener);
    }

    public boolean callListeners(WorldAccess world, EnderlingStructure structure, BlockPos root) {
        for (OnStructureGenerate.Listener listener : this.listeners) {
            ActionResult result = listener.onStructureGenerate(world, structure, root);
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
