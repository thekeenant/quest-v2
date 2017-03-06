package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FieldParam implements Param {
    private final List<String> fields;

    public FieldParam(List<String> fields) {
        this.fields = fields;
    }

    public FieldParam(String... fields) {
        this(Arrays.asList(fields));
    }

    @Override
    public String getKey() {
        StringBuilder sb = new StringBuilder();
        for (String field : this.fields) {
            // Backticks or double quotes are NOT used intentionally, so as to support SQL standard AND MySQL
            sb.append(field);
            if (!this.fields.get(this.fields.size() - 1).equals(field)) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    public List<Object> getObjects() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "FieldParam(fields=" + this.fields + ")";
    }
}
