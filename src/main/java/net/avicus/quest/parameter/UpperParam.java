package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class UpperParam implements Param {
    private final Param child;

    public UpperParam(Param child) {
        this.child = child;
    }

    public UpperParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getParamString() {
        return "UPPER(" + this.child.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.child.getValues();
    }

    @Override
    public String toString() {
        return "UpperParam(child=" + this.child + ")";
    }
}
