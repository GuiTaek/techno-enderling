package com.gmail.guitaekm.technoenderling.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class ArbitraryStructure {
    public final List<PaletteElement> palette;
    public final Map<Vec3i, Integer> structure;
    public final List<Vec3i> checkOffsets;
    public final Vec3i size;

    public ArbitraryStructure(NbtCompound nbt, TagManager tagManager) {
        NbtList sizeNbt = nbt.getList("size", NbtElement.INT_TYPE);
        this.size = new Vec3i(sizeNbt.getInt(0), sizeNbt.getInt(1), sizeNbt.getInt(2));
        NbtList paletteRaw = nbt.getList("palette", NbtElement.COMPOUND_TYPE);
        this.structure = new HashMap<>();
        this.palette = new ArrayList<>();
        this.checkOffsets = new ArrayList<>();
        for (NbtElement elem : paletteRaw) {
            if (!(elem instanceof NbtCompound)) {
                throw new IllegalArgumentException("Palette element is not a nbtCompound");
            }
            palette.add(PaletteElement.create(tagManager, (NbtCompound) elem));
        }
        NbtList blocks = nbt.getList("blocks", NbtElement.COMPOUND_TYPE);
        for (NbtElement block : blocks) {
            if (block instanceof NbtCompound blockCompound) {
                NbtList nbtPos = blockCompound.getList("pos", NbtElement.INT_TYPE);
                Vec3i pos = new Vec3i(nbtPos.getInt(0), nbtPos.getInt(1), nbtPos.getInt(2));
                this.structure.put(pos, blockCompound.getInt("state"));
                continue;
            }
            throw new IllegalArgumentException("The structure has wrong format");
        }
        if(!nbt.contains("offsets")) {
            // so they also work when just a vanilla structure (kind of)
            checkOffsets.add(new Vec3i(0, 0, 0));
            return;
        }
        for (NbtElement rawOffset : nbt.getList("offsets", NbtElement.LIST_TYPE)) {
            NbtList offset = (NbtList) rawOffset;
            this.checkOffsets.add(new Vec3i(offset.getInt(0), offset.getInt(1), offset.getInt(2)));
        }
    }
    public void place(WorldAccess world, BlockPos pos, Vec3i offset, int flags) {
        for (Map.Entry<Vec3i, Integer> entry : this.structure.entrySet()) {
            PaletteElement elem = this.palette.get(entry.getValue());
            BlockState toPlaceState = elem.getState();
            if (toPlaceState == null) {
                Tag<Block> tag = elem.getTag();
                assert tag != null;
                toPlaceState = tag.getRandom(world.getRandom()).getDefaultState();
            }
            world.setBlockState(pos.subtract(offset).add(entry.getKey()), toPlaceState, flags);
        }
    }
    public Optional<BlockPos> check(WorldAccess world, BlockPos pos) {
        List<Vec3i> offsetsShuffled = new ArrayList<>(this.checkOffsets);
        Collections.shuffle(offsetsShuffled, world.getRandom());
        for (Vec3i offset : offsetsShuffled) {
            if (this.checkWithOffset(world, pos, offset)) {
                return Optional.of(pos.subtract(offset));
            }
        }
        return Optional.empty();
    }
    public boolean checkWithOffset(WorldAccess world, BlockPos pos, Vec3i offset) {
        BlockPos alteredPos = pos.subtract(offset);
        for (Map.Entry<Vec3i, Integer> entry : this.structure.entrySet()) {
            BlockState toTestState = world.getBlockState(alteredPos.add(entry.getKey()));
            PaletteElement elem = this.palette.get(entry.getValue());
            BlockState toCompareState = elem.getState();
            if (toCompareState != null) {
                if (!toTestState.equals(toCompareState)) {
                    return false;
                }
                continue;
            }
            Tag<Block> tag = elem.getTag();
            assert tag != null;
            if (!tag.contains(toTestState.getBlock())) {
                return false;
            }
        }
        return true;
    }
}
