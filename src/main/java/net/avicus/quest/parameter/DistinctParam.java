package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class DistinctParam implements Param {
    private final Param child;

    public DistinctParam(Param child) {
        this.child = child;
    }

    public DistinctParam(Object object) {
        this(new ObjectParam(object));
    }

    public DistinctParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getParamString() {
        return "DISTINCT " + this.child.getParamString();
    }

    public List<Object> getValues() {
        return this.child.getValues();
    }

    @Override
    public String toString() {
        return "DistinctParam(child=" + this.child + ")";
    }
}
