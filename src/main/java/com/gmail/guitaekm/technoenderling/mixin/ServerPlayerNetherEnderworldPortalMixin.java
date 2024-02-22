package com.gmail.guitaekm.technoenderling.mixin;

import com.gmail.guitaekm.technoenderling.access.IServerPlayerNetherEnderworldPortal;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerNetherEnderworldPortalMixin implements IServerPlayerNetherEnderworldPortal {
    // configure
    @Unique
    private static String DEFAULT_NAME = "portal-";
    // configure
    @Unique
    private static String FALLBACK_NAME = "no-new-name-found";
    //configure
    @Unique
    private static int BATCH_SIZE = 1000;
    @Unique
    private final List<EnderworldPortalBlock.NetherInstance> savedDestinations = new ArrayList<>();
    @Unique
    private EnderworldPortalBlock.NetherInstance source = null;

    @Override
    public void techno_enderling$remove(EnderworldPortalBlock.NetherInstance portal) {
        this.savedDestinations.remove(portal);
    }

    @Override
    public void techno_enderling$add(EnderworldPortalBlock.NetherInstance portal) {
        this.savedDestinations.add(portal);
    }

    @Override
    public List<EnderworldPortalBlock.NetherInstance> techno_enderling$getDestinations() {
        return this.savedDestinations;
    }

    @Override
    public void techno_enderling$setSource(EnderworldPortalBlock.@Nullable NetherInstance source) {
        this.source = source;
    }

    @Override
    public EnderworldPortalBlock.NetherInstance techno_enderling$addIfNotPresent(BlockPos pos) {
        List<EnderworldPortalBlock.NetherInstance> possibleInstances = this.savedDestinations
                .stream()
                .filter(instance -> pos.equals(instance.pos()))
                .toList();
        if (possibleInstances.isEmpty()) {
            Integer resultIndex = null;
            // average complexity: theta(n)
            Set<String> savedNamesSet = new HashSet<>(
                    this.savedDestinations.stream().map(instance -> instance.name()).toList()
            );
            String resultName = null;
            for (int i = 0; i < 10; ++i) {
                Set<String> possibleNames = IntStream.range(BATCH_SIZE * i, BATCH_SIZE * i + BATCH_SIZE)
                        .boxed()
                        .map(id ->DEFAULT_NAME + (id + 1))
                        .collect(Collectors.toSet());
                // average complexity: theta(n)
                // source: https://www.baeldung.com/java-hashset-removeall-performance
                possibleNames.removeAll(savedNamesSet);
                if (!possibleNames.isEmpty()) {
                    resultName =  possibleNames
                            .stream().min(new Comparator<String>() {
                                public int extractNumber(String val) {
                                    return Integer.parseInt(val.substring(DEFAULT_NAME.length()));
                                }
                                @Override
                                public int compare(String o1, String o2) {
                                    return Integer.compare(extractNumber(o1), extractNumber(o2));
                                }
                            }).get();
                    break;
                }
            }
            if (resultName == null) {
                resultName = FALLBACK_NAME;
            }
            EnderworldPortalBlock.NetherInstance result = new EnderworldPortalBlock.NetherInstance(
                    this.savedDestinations.size(),
                    resultName,
                    pos
            );
            this.savedDestinations.add(result);
            return result;
        }
        assert (possibleInstances.size() == 1);
        return possibleInstances.get(0);
    }

    @Override
    public @Nullable EnderworldPortalBlock.NetherInstance techno_enderling$getSource() {
        return this.source;
    }

    // todo: probably needs to be in a util class
    private static void putPos(String name, NbtCompound nbt, BlockPos pos) {
        NbtCompound posNbt = new NbtCompound();
        posNbt.putInt("x", pos.getX());
        posNbt.putInt("y", pos.getY());
        posNbt.putInt("z", pos.getZ());
        nbt.put(name, posNbt);
    }

    // todo: probably needs to be in a util class
    private static BlockPos getPos(String name, NbtCompound nbt) {
        NbtCompound nbtPos = nbt.getCompound(name);
        return new BlockPos(nbtPos.getInt("x"), nbtPos.getInt("y"), nbtPos.getInt("z"));
    }

    // todo: probably needs to be in a util class
    private static NbtCompound instanceToNbt(EnderworldPortalBlock.NetherInstance instance) {
        NbtCompound nbtInstance = new NbtCompound();
        nbtInstance.putString("name", instance.name());
        putPos("pos", nbtInstance, instance.pos());
        nbtInstance.putInt("id", instance.id());
        return nbtInstance;
    }

    // todo: probably needs to be in a util class
    private static EnderworldPortalBlock.NetherInstance nbtToInstance(NbtCompound nbtInstance) {
        String instanceName = nbtInstance.getString("name");
        BlockPos pos = getPos("pos", nbtInstance);
        int id = nbtInstance.getInt("id");
        return new EnderworldPortalBlock.NetherInstance(id, instanceName, pos);
    }
    @Inject(method = "writeCustomDataToNbt", at=@At("TAIL"))
    public void writeCustomDataToNbtTail(NbtCompound nbt, CallbackInfo ci) {
        NbtList nbtDestinations = new NbtList();
        this.savedDestinations.forEach(
                netherInstance -> nbtDestinations.add(instanceToNbt(netherInstance))
        );
        nbt.put("netherDestinations", nbtDestinations);
    }

    @Inject(method = "readCustomDataFromNbt", at=@At("TAIL"))
    public void readCustomDataFromNbtTail(NbtCompound nbt, CallbackInfo ci) {
        this.savedDestinations.clear();
        NbtList nbtDesinations = nbt.getList("netherDestinations", NbtElement.COMPOUND_TYPE);
        this.savedDestinations.addAll(
                nbtDesinations
                        .stream()
                        .map(nbtElement -> nbtToInstance((NbtCompound) nbtElement))
                        .toList()
        );
    }
}
