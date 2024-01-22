package com.gmail.guitaekm.technoenderling.features;

import com.gmail.guitaekm.technoenderling.utils.StructureIter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;


public class ConvertibleDatapackStructure {
    private @Nullable List<List<List<String>>> structure;
    private @Nullable List<Vec3i> offsets;
    // this class has two "init stages" because at the time, this is created, possibly no server exists
    private boolean isReady;
    final private JsonArray structureJson;
    final private JsonArray offsetsJson;
    private Vec3i size;
    // having it unchecked helps not to have the error thrown twice
    public static class NoTagKnownException extends RuntimeException {
        public NoTagKnownException(String tag) {
            super(MessageFormat.format("The tag \"{0}\" isn't known!", tag));
        }
    }
    public ConvertibleDatapackStructure(JsonElement content) {
        JsonObject obj = content.getAsJsonObject();
        this.structureJson = obj.getAsJsonArray("structure");
        this.offsetsJson = obj.getAsJsonArray("offsets");
        this.isReady = false;
        this.offsets = null;
        this.structure = null;
        this.size = null;
    }

    protected void lazyInit(MinecraftServer server) {
        if (!this.isReady) {
            this.size = ConvertibleDatapackStructure.parseSize(this.structureJson);
            this.offsets = ConvertibleDatapackStructure.parseOffsets(this.size, this.offsetsJson);
            this.structure = ConvertibleDatapackStructure.parseStructure(server, this.structureJson);
        }
        this.isReady = true;
    }
    protected static List<Vec3i> parseOffsets(Vec3i size, JsonArray arr) {
        List<Vec3i> result = new ArrayList<>();
        for(JsonElement offset : arr) {
            Iterator<JsonElement> vecJson = offset.getAsJsonArray().iterator();
            int userX = vecJson.next().getAsInt();
            int userY = vecJson.next().getAsInt();
            int userZ = vecJson.next().getAsInt();
            // as I add the offset, x and z have to be negative
            int resX = -userX;
            int resZ = -userZ;
            // switch y -- else, the offsets would be upside down
            int resY = size.getY() - userY - 1;
            result.add(new Vec3i(resX, resY, resZ));
            assert !vecJson.hasNext();
        }
        return result;
    }
    protected static Vec3i parseSize(JsonArray arr) {
        JsonArray yArr = arr;
        JsonArray xArr = yArr.get(0).getAsJsonArray();
        JsonArray zArr = xArr.get(0).getAsJsonArray();
        return new Vec3i(xArr.size(), yArr.size(), zArr.size());
    }
    protected static List<List<List<String>>> parseStructure(MinecraftServer server, JsonArray arr) {
        List<List<List<String>>> structure = new ArrayList<>();
        for(JsonElement layer : arr) {        // goes from top to bottom
            List<List<String>> layerList = new ArrayList<>();
            for(JsonElement row : layer.getAsJsonArray()) {
                List<String> rowList = new ArrayList<>();
                for(JsonElement col : row.getAsJsonArray()) {
                    String toAdd = col.getAsString();
                    ConvertibleDatapackStructure.assertValidString(server, toAdd);
                    rowList.add(toAdd);
                }
                layerList.add(rowList);
            }
            structure.add(layerList);
        }
        return structure;
    }
    protected static void assertValidString(MinecraftServer server, String toCheck) {
        toCheck = toCheck.strip();
        if (toCheck.isEmpty()) {
            return;
        }
        String[] out = toCheck.split(":");
        if (out.length != 2) {
            throw new IllegalArgumentException("Expects for the blocks two names, separated by a colon");
        }
        if (out[0].charAt(0) == '#') {
            server.getTagManager().getTag(
                    Registry.BLOCK_KEY,
                    new Identifier(out[0].substring(1), out[1]),
                    id -> new NoTagKnownException(id.toString()));
            return;
        }
        Registry.BLOCK.get(new Identifier(out[0], out[1]));
    }
    protected static boolean checkBlock(ServerWorld server, BlockPos pos, String toCheck) {
        // empty strings won't replace existing Blocks
        toCheck = toCheck.strip();
        if (toCheck.isEmpty()) {
            return true;
        }
        String[] out = toCheck.split(":");
        if (out.length != 2) {
            throw new IllegalArgumentException("Expects for the blocks two names, separated by a colon");
        }
        Block blockToCheck = server.getBlockState(pos).getBlock();
        if (out[0].charAt(0) == '#') {
            return server
                    .getTagManager()
                    .getTag(
                        Registry.BLOCK_KEY,
                        new Identifier(out[0].substring(1), out[1]),
                        id -> new NoTagKnownException(id.toString())
                    )
                    .contains(blockToCheck);
        }
        return Registry.BLOCK.get(new Identifier(out[0], out[1])) == blockToCheck;
    }

    /**
     * generates the structure at Position pos
     * @param server the minecraft server
     * @param pos the position of the center block
     * @param offset the relative position of the top corner to the center block (remember that this class
     *               assumes symmetry)
     */
    public boolean checkStructureOnPos(ServerWorld server, BlockPos pos, Vec3i offset) {
        this.lazyInit(server.getServer());
        assert this.structure != null;
        pos = pos.add(offset);
        ListIterator<List<List<String>>> layerIter = this.structure.listIterator();
        return StructureIter.iterStructureList(
                this.structure,
                pos,
                (position, value) -> ConvertibleDatapackStructure.checkBlock(server, position, value)
        ).stream().filter((Boolean res) -> !res).findFirst().isEmpty();
    }
    public Vec3i size() {
        int y = this.structure.size();
        int x = this.structure.get(0).size();
        int z = this.structure.get(0).get(0).size();
        return new Vec3i(x, y, z);
    }
    public Optional<BlockPos> findStructureToConvert(ServerWorld server, BlockPos pos) {
        this.lazyInit(server.getServer());
        assert this.offsets != null;
        assert this.structure != null;
        // this helps with multiple possible structures
        Collections.shuffle(this.offsets, server.toServerWorld().getRandom());
        for(Vec3i offset : this.offsets) {
            if (this.checkStructureOnPos(server, pos, offset)) {
                Vec3i resOffset = offset.subtract(new Vec3i(this.size().getX() - 1, this.size().getY(), this.size().getZ()));

                return Optional.of(pos.add(offset));
            }
        }
        return Optional.empty();
    }
}
