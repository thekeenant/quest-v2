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
    public Optional<T> map(Record field) {
        return field.get(name).map(obj -> (T) obj);
    }

    @SuppressWarnings("unchecked")
    public T mapNonNull(Record field) {
        return (T) field.getNonNull(name);
    }

    public String getName() {
        return name;
    }

    public static <T> Column<T> of(String name) {
        return new Column<>(name);
    }
}
