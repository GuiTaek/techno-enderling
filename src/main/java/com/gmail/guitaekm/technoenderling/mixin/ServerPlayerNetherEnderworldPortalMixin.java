package com.gmail.guitaekm.technoenderling.mixin;

import com.gmail.guitaekm.technoenderling.access.IServerPlayerNetherEnderworldPortal;
import com.gmail.guitaekm.technoenderling.blocks.EnderworldPortalBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerNetherEnderworldPortalMixin implements IServerPlayerNetherEnderworldPortal {
    // configure
    @Unique
    private static String DEFAULT_NAME = "portal-";
    @Unique
    private static String FALLBACK_NAME = "no-new-name-found";
    //configure
    @Unique
    private static int BATCH_SIZE = 10;
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
            savedNamesSet = IntStream.range(0, 100_000)
                    .boxed()
                    .map(id ->DEFAULT_NAME + id)
                    .collect(Collectors.toSet());
            String resultName = null;
            // average complexity: theta(n)
            // source: https://www.baeldung.com/java-hashset-removeall-performance
            for (int i = 0; i < 10; ++i) {
                Set<String> possibleNames = IntStream.range(BATCH_SIZE * i, BATCH_SIZE * i + BATCH_SIZE)
                        .boxed()
                        .map(id ->DEFAULT_NAME + id)
                        .collect(Collectors.toSet());
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
}
