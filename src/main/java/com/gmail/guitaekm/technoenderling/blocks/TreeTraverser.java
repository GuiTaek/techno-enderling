package com.gmail.guitaekm.technoenderling.blocks;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TreeTraverser<T> {
    List<TreeTraverser<T>> childs;
    T vertex;

    protected TreeTraverser(T vertex, List<TreeTraverser<T>> childs) {
        this.childs = childs;
        this.vertex = vertex;
    }

    public static <T> TreeTraverser<T> parseVertex(
            T root,
            Function<T, List<T>> childExtractor,
            UnaryOperator<T> operateOnEntity
    ) {
        List<TreeTraverser<T>> childs = childExtractor
                .apply(root)
                .stream()
                .map(
                        (T child) -> TreeTraverser.parseVertex(
                                child,
                                childExtractor,
                                operateOnEntity
                        )
                ).toList();

        return new TreeTraverser<>(operateOnEntity.apply(root), childs);
    }

    public void depthFirstSearch(BiConsumer<T, T> consumeRelations) {
        for (TreeTraverser<T> child : this.childs) {
            child.depthFirstSearch(consumeRelations);
            consumeRelations.accept(this.vertex, child.vertex);
        }
    }
}
