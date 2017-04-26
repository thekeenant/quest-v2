package net.avicus.quest;

import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.ObjectParam;

import java.util.Optional;

public class Column<T> extends FieldParam {
    private final String name;

    public Column(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public Param wrapObject(Object object) {
        return new ObjectParam(object.toString());
    }

    @SuppressWarnings("unchecked")
    public Optional<T> map(RecordField field) {
        return field.asObject().map(obj -> (T) obj);
    }

    public final Optional<T> map(Record record) {
        return map(record.field(name));
    }

    @SuppressWarnings("unchecked")
    public T mapNonNull(RecordField field) {
        return (T) field.asNonNullObject();
    }

    @SuppressWarnings("unchecked")
    public final T mapNonNull(Record record) {
        return mapNonNull(record.field(name));
    }

    public String getName() {
        return name;
    }

    public static <T> Column<T> of(String name) {
        return new Column<>(name);
    }
}
