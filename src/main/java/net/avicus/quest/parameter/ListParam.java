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
    private final List<Param> parameters;

    public ListParam(List<Param> parameters) {
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException("List parameter must have at least one parameter.");
        }
        this.parameters = parameters;
    }

    @Override
    public String getKey() {
        StringBuilder sb = new StringBuilder("(");
        for (Param parameter : this.parameters) {
            sb.append(parameter.getKey());
            if (this.parameters.indexOf(parameter) != this.parameters.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public List<Object> getObjects() {
        List<Object> values = new ArrayList<>();
        for (Param parameter : this.parameters) {
            values.addAll(parameter.getObjects());
        }
        return values;
    }

    @Override
    public String toString() {
        return "ListParam(key=" + getKey() + ", values=" + getObjects() + ")";
    }
}
