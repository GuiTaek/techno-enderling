package com.gmail.guitaekm.endergenesis.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TreeTraverser<T> {
    private List<TreeTraverser<T>> childs;
    private T vertex;

    protected TreeTraverser(T vertex, List<TreeTraverser<T>> childs) {
        this.childs = childs;
        this.vertex = vertex;
    }

    public T getVertex() {
        return vertex;
    }

    public TreeTraverser<T> addChild(T vertex) {
        TreeTraverser<T> child = new TreeTraverser<>(vertex, new ArrayList<>());
        this.childs.add(child);
        return child;
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

    public <R> TreeTraverser<R> map(Function<T, R> func) {
        List<TreeTraverser<R>> childs = this.childs.stream().map(
                (TreeTraverser<T> child) -> (child.map(func))
        ).toList();

        return new TreeTraverser<>(func.apply(this.vertex), childs);
    }
}
