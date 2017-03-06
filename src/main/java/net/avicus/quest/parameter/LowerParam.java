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
    public String getKey() {
        return "LOWER(" + this.child.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.child.getObjects();
    }

    @Override
    public String toString() {
        return "LowerParam(child=" + this.child + ")";
    }
}
