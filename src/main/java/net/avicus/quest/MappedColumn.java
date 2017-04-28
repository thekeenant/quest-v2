package net.avicus.quest;

import net.avicus.quest.parameter.ObjectParam;

import java.util.Optional;
import java.util.function.Function;

public class MappedColumn<I, T> extends Column<T> {
    private final String name;
    private final Function<I, T> mapper;
    private final Function<T, I> reverse;
    private final Class<I> sqlType;
    private final Class<T> mappedType;

    protected MappedColumn(String name, Class<I> sqlType, Class<T> mappedType, Function<I, T> mapper, Function<T, I> reverse) {
        super(name);
        this.name = name;
        this.sqlType = sqlType;
        this.mappedType = mappedType;
        this.mapper = mapper;
        this.reverse = reverse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Param wrapObject(Object object) {
        if (sqlType.isInstance(object)) {
            return super.wrapObject(object);
        }
        else if (mappedType.isInstance(object)) {
            I sqlType = fromMappedType((T) object);
            return new ObjectParam(sqlType);
        }

        throw new IllegalArgumentException("Cannot use type " + object.getClass().getSimpleName() + " for filter");
    }

    public T toMappedType(I raw) {
        return mapper.apply(raw);
    }

    public I fromMappedType(T mapped) {
        return reverse.apply(mapped);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> map(Record field) {
        return field.get(name).map(obj -> toMappedType((I) obj));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T mapNonNull(Record field) {
        return mapper.apply((I) field.getNonNull(name));
    }

    public static <I, T> MappedColumn<I, T> of(String name, Class<I> sqlType, Class<T> mappedType, Function<I, T> mapper, Function<T, I> reverse) {
        return new MappedColumn<>(name, sqlType, mappedType, mapper, reverse);
    }
}
