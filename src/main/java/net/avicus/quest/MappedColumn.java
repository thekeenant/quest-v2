package net.avicus.quest;

import java.util.Optional;
import java.util.function.Function;

public class MappedColumn<I, T> extends Column<T> {
    private final String name;
    private final ColumnMapper<I, T> mapper;
    private final Function<T, I> reverse;

    public MappedColumn(String name, ColumnMapper<I, T> mapper, Function<T, I> reverse) {
        super(name);
        this.name = name;
        this.mapper = mapper;
        this.reverse = reverse;
    }

    public T toMappedType(Row row, I raw) {
        return mapper.apply(row, raw);
    }

    public I fromMappedType(T mapped) {
        return reverse.apply(mapped);
    }

    @SuppressWarnings("unchecked")
    public Optional<T> get(Row row) {
        return row.get(name).asObject().map(obj -> mapper.apply(row, (I) obj));
    }

    @SuppressWarnings("unchecked")
    public T getRequired(Row row) {
        return mapper.apply(row, (I) row.get(name).asRequiredObject());
    }

    public static <I, T> MappedColumn<I, T> of(String name, ColumnMapper<I, T> mapper, Function<T, I> reverse) {
        return new MappedColumn<>(name, mapper, reverse);
    }
}
