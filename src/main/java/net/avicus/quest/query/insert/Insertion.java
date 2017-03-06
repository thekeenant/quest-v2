package net.avicus.quest.query.insert;

import net.avicus.quest.Param;
import net.avicus.quest.Row;
import net.avicus.quest.parameter.NullParam;
import net.avicus.quest.parameter.ObjectParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Insertion {
    private final Map<String, Param> values;

    public Insertion(Map<String, Param> values) {
        this.values = values;
    }

    public Insertion duplicate() {
        return new Insertion(new HashMap<>(this.values));
    }

    public Insertion with(String column, Param value) {
        Insertion insertion = duplicate();
        insertion.values.put(column, value);
        return insertion;
    }

    public Insertion with(String column, Object value) {
        return with(column, new ObjectParam(value));
    }

    public Set<String> getColumns() {
        return this.values.keySet();
    }

    public Param getValue(String column) {
        return this.values.containsKey(column) ? this.values.get(column) : NullParam.INSTANCE;
    }

    public static Insertion fromRow(Row row) {
        Map<String, Object> data = row.toMap();
        Map<String, Param> converted = new HashMap<>();
        for (Entry<String, Object> entry : data.entrySet()) {
            converted.put(entry.getKey(), new ObjectParam(entry.getValue()));
        }
        return new Insertion(converted);
    }

    public static InsertionBuilder builder() {
        return new InsertionBuilder();
    }

    public static class InsertionBuilder {
        private final Map<String, Param> values;

        private InsertionBuilder() {
            this.values = new HashMap<>();
        }

        public InsertionBuilder value(String column, Param value) {
            this.values.put(column, value);
            return this;
        }

        public InsertionBuilder value(String column, Object value) {
            return value(column, new ObjectParam(value));
        }

        public InsertionBuilder values(Map<String, Param> values) {
            this.values.putAll(values);
            return this;
        }

        public Insertion build() {
            return new Insertion(this.values);
        }
    }
}
