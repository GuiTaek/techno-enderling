package com.gmail.guitaekm.technoenderling.features;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class EnderlingStructure {
    protected PlaceableDatapackStructure placeable;
    protected ConvertibleDatapackStructure convertible;
    protected Identifier id;
    public EnderlingStructure(JsonElement initJson) {
        JsonObject obj = initJson.getAsJsonObject();
        JsonElement placeable = obj.get("placeable");
        this.placeable = new PlaceableDatapackStructure(placeable);
        JsonElement convertible = obj.get("convertible");
        this.convertible = new ConvertibleDatapackStructure(convertible);
        JsonElement id = obj.get("id");
        String[] out = id.getAsString().split(":");
        assert out.length == 2;
        this.id = new Identifier(out[0], out[1]);
    }

    public PlaceableDatapackStructure getPlaceable() {
        return this.placeable;
    }

    public Identifier getId() {
        return this.id;
    }

    public static boolean isSurfaceBlock(BlockState state) {
        return state.getBlock() == Blocks.END_STONE;
    }
}
