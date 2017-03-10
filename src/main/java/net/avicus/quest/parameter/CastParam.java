package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class CastParam implements Param {
    private final Param child;
    private final CastType type;

    public CastParam(Param child, CastType type) {
        this.child = child;
        this.type = type;
    }

    public CastParam(String column, CastType type) {
        this(new FieldParam(column), type);
    }

    @Override
    public String getParamString() {
        return "CAST(" + this.child.getParamString() + " AS " + this.type.toString() + ")";
    }

    public List<Object> getValues() {
        return this.child.getValues();
    }

    @Override
    public String toString() {
        return "CastParam(child=" + this.child + ")";
    }

    public enum CastType {
        BINARY
    }
}
