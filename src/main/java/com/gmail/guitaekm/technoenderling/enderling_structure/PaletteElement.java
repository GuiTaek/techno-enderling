package com.gmail.guitaekm.technoenderling.enderling_structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class PaletteElement {
    public enum Type {
        BLOCK_STATE, TAG
    }

    @Nullable
    private BlockState state;
    @Nullable
    private Tag<Block> tag;
    private Type type;

    public PaletteElement(BlockState state) {
        this.state = state;
        this.tag = null;
        this.type = Type.BLOCK_STATE;
    }

    public PaletteElement(Tag<Block> tag) {
        this.state = null;
        this.tag = tag;
        this.type = Type.TAG;
    }

    public static PaletteElement create(TagManager tagManager, NbtCompound nbt) {
        if (!nbt.contains("is_tag") || !nbt.getBoolean("is_tag")) {
            return new PaletteElement(NbtHelper.toBlockState(nbt));
        }
        String[] out = nbt.getString("tag").split(":");
        if (out[0].charAt(0) != '#') {
            throw new IllegalArgumentException(("Missing # for tags"));
        }
        if (out.length != 2) {
            throw new IllegalArgumentException("Tags are of the format #<namespace>:<tag name>");
        }
        return new PaletteElement(
                tagManager.getTag(
                        Registry.BLOCK_KEY,
                        new Identifier(out[0].substring(1), out[1]),
                        id -> new IllegalArgumentException(id.toString()))
        );

    }

    public @Nullable BlockState getState() {
        return this.state;
    }

    public @Nullable Tag<Block> getTag() {
        return this.tag;
    }

    public Type getType() {
        return this.type;
    }
}
