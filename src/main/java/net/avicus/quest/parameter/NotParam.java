package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class NotParam implements Param {
    private final Param child;

    public NotParam(Param child) {
        this.child = child;
    }

    @Override
    public String getParamString() {
        return "NOT " + this.child.getParamString();
    }

    @Override
    public List<Object> getValues() {
        return this.child.getValues();
    }

    @Override
    public String toString() {
        return "NotParam(child=" + this.child + ")";
    }
}
