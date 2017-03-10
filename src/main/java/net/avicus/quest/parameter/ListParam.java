package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list in SQL, for example:
 *
 * SELECT * FROM `users` WHERE `id` IN (?, ?, ?);
 */
public class ListParam implements Param {
    private final List<Param> items;

    public ListParam(List<Param> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("List parameter must have at least one parameter.");
        }
        this.items = items;
    }

    @Override
    public String getParamString() {
        StringBuilder sb = new StringBuilder("(");
        for (Param param : this.items) {
            sb.append(param.getParamString());
            if (this.items.indexOf(param) != this.items.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        for (Param parameter : this.items) {
            values.addAll(parameter.getValues());
        }
        return values;
    }

    @Override
    public String toString() {
        return "ListParam(key=" + getParamString() + ", values=" + getValues() + ")";
    }
}
