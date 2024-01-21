package com.gmail.guitaekm.technoenderling.utils;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StructureIter {
    public interface IterCallback <T, R> {
        R map(BlockPos position, T value);
    }
    public static <T, R> List<R> iterStructureList(List<List<List<T>>> structure, BlockPos pos, IterCallback<T, R> action) {
        List<R> result = new ArrayList<>();
        ListIterator<List<List<T>>> layerIter = structure.listIterator();
        while(layerIter.hasNext()) {
            int y = layerIter.nextIndex();
            List<List<T>> layer = layerIter.next();
            ListIterator<List<T>> rowIter = layer.listIterator();
            while(rowIter.hasNext()) {
                int x = rowIter.nextIndex();
                List<T> row = rowIter.next();
                ListIterator<T> colIter = row.listIterator();
                while(colIter.hasNext()) {
                    int z = colIter.nextIndex();
                    T value = colIter.next();
                    // remember, from top to bottom, therefore "-y"
                    BlockPos toConsume = pos.add(x, -y, z);
                    result.add(action.map(toConsume, value));
                }
            }
        }
        return result;
    }
}
