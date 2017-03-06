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
    public String getKey() {
        return "UPPER(" + this.child.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.child.getObjects();
    }

    @Override
    public String toString() {
        return "UpperParam(child=" + this.child + ")";
    }
}
