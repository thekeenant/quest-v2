package net.avicus.quest.table;

import net.avicus.quest.parameter.FieldParam;

import java.util.function.Function;

public class MappedColumn<I, O> extends FieldParam {
    private final String field;
    private final Function<I, O> mapper;

    public MappedColumn(String field, Function<I, O> mapper) {
        this.field = field;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public MappedColumn(String field) {
        this(field, i -> (O) i);
    }

    public O map(I input) {
        return this.mapper.apply(input);
    }

    public String getField() {
        return this.field;
    }
}
