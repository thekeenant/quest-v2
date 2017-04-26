package net.avicus.quest;

import net.avicus.quest.parameter.FieldParam;

import java.util.Optional;

public class Column<T> extends FieldParam {
    private final String name;

    public Column(String name) {
        super(name);
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public Optional<T> get(Row row) {
        return row.get(name).asObject().map(obj -> (T) obj);
    }

    @SuppressWarnings("unchecked")
    public T getRequired(Row row) {
        return (T) row.get(name).asRequiredObject();
    }

    public String getName() {
        return name;
    }

    public static <T> Column<T> of(String name) {
        return new Column<>(name);
    }
}
