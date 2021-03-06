package net.avicus.quest;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ColumnMapper<I, O> extends BiFunction<Row, I, O> {
}
