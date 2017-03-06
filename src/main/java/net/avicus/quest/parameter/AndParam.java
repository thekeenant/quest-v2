package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.ArrayList;
import java.util.List;

public class AndParam implements Param {
    private final Param first;
    private final Param second;
    private final List<Object> values;

    public AndParam(Param first, Param second) {
        this.first = first;
        this.second = second;
        this.values = new ArrayList<>();
        this.values.addAll(first.getObjects());
        this.values.addAll(second.getObjects());
    }

    public AndParam(int first, int second) {
        this(new ObjectParam(first), new ObjectParam(second));
    }

    @Override
    public String getKey() {
        return "(" + this.first.getKey() + " AND " + this.second.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.values;
    }

    @Override
    public String toString() {
        return "AndParam(first=" + this.first + ", second=" + this.second + ")";
    }
}
