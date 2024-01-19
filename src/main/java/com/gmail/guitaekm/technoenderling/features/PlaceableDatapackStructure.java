package com.gmail.guitaekm.technoenderling.features;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * assumes symmetry, therefore doesn't check if rotating the structure works
 */
public class PlaceableDatapackStructure {
    private List<List<List<@Nullable BlockState>>> structure;
    public PlaceableDatapackStructure(JsonElement elem) {
        JsonArray arr = elem.getAsJsonArray();
        structure = new ArrayList<>();
        for(JsonElement layer : arr) {        // goes from top to bottom
            List<List<BlockState>> layerList = new ArrayList<>();
            for(JsonElement row : layer.getAsJsonArray()) {
                List<BlockState> rowList = new ArrayList<>();
                for(JsonElement col : row.getAsJsonArray()) {
                    rowList.add(getBlockState(col.getAsString()));
                }
                layerList.add(rowList);
            }
            structure.add(layerList);
        }
    }
    protected static @Nullable BlockState getBlockState(String content) {
        // empty strings won't replace existing Blocks
        if (content.strip() == "") {
            return null;
        }
        String[] vals = content.split(":");
        if (vals.length != 2) {
            throw new IllegalArgumentException("Expects for the blocks two names, separated by a colon");
        }
        return Registry.BLOCK.get(new Identifier(vals[0], vals[1])).getDefaultState();
    }

    /**
     * generates the structure at Position pos
     * @param world
     * @param pos the postion of the center block
     */
    public void generate(StructureWorldAccess world, BlockPos pos) {
        ListIterator<List<List<BlockState>>> layerIter = this.structure.listIterator();
        while(layerIter.hasNext()) {
            int y = layerIter.nextIndex();
            List<List<BlockState>> layer = layerIter.next();
            ListIterator<List<BlockState>> rowIter = layer.listIterator();
            while(rowIter.hasNext()) {
                int x = rowIter.nextIndex();
                List<BlockState> row = rowIter.next();
                ListIterator<BlockState> colIter = row.listIterator();
                while(colIter.hasNext()) {
                    int z = colIter.nextIndex();
                    BlockState state = colIter.next();
                    if (state != null) {
                        // remember, from top to bottom, therefore "-y"
                        BlockPos toPlacePos = pos.add(x, -y, z);
                        world.setBlockState(toPlacePos, state, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }
}
