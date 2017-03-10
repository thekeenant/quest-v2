package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class LowerParam implements Param {
    private final Param child;

    public LowerParam(Param child) {
        this.child = child;
    }

    public LowerParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getParamString() {
        return "LOWER(" + this.child.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.child.getValues();
    }

    @Override
    public String toString() {
        return "LowerParam(child=" + this.child + ")";
    }
}
