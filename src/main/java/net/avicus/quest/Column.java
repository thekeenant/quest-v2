package net.avicus.quest;

import net.avicus.quest.parameter.FieldParam;

import java.util.Optional;

public class Column<T> extends FieldParam {
    private final String name;
    private final ColumnMapper<Object, T> mapper;

    public Column(String name, ColumnMapper<Object, T> mapper) {
        super(name);
        this.name = name;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public Column(String name) {
        this(name, (row, in) -> (T) in);
    }

    public Optional<T> get(Row row) {
        return row.get(name).asObject().map(obj -> mapper.apply(row, obj));
    }

    public T getRequired(Row row) {
        Object obj = row.get(name).asRequiredObject();
        return mapper.apply(row, obj);
    }

    public String getName() {
        return name;
    }

    public static <T> Column<T> of(String name) {
        return new Column<>(name);
    }

    @SuppressWarnings("unchecked")
    public static <I, T> Column<T> of(String name, ColumnMapper<I, T> map) {
        ColumnMapper<Object, T> castedMapper = (row, obj) -> map.apply(row, (I) obj);
        return new Column<>(name, castedMapper);
    }
}
