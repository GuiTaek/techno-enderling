package com.gmail.guitaekm.technoenderling.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class VehicleRecursionStructure<T> {
    List<VehicleRecursionStructure<T>> childs;
    T vertex;

    protected VehicleRecursionStructure(T vertex, List<VehicleRecursionStructure<T>> childs) {
        this.childs = childs;
        this.vertex = vertex;
    }

    public static <T> VehicleRecursionStructure<T> parseVertex(
            T root,
            Function<T, List<T>> childExtractor,
            UnaryOperator<T> operateOnEntity
    ) {
        List<VehicleRecursionStructure<T>> childs = childExtractor
                .apply(root)
                .stream()
                .map(
                        (T child) -> VehicleRecursionStructure.parseVertex(
                                child,
                                childExtractor,
                                operateOnEntity
                        )
                ).toList();

        return new VehicleRecursionStructure<>(operateOnEntity.apply(root), childs);
    }

    public void depthFirstSearch(BiConsumer<T, T> consumeRelations) {
        for (VehicleRecursionStructure<T> child : this.childs) {
            child.depthFirstSearch(consumeRelations);
            consumeRelations.accept(this.vertex, child.vertex);
        }
    }
}
