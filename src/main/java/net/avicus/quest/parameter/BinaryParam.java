package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class BinaryParam implements Param {
    private final Param child;

    public BinaryParam(Param child) {
        this.child = child;
    }

    public BinaryParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getKey() {
        return "BINARY " + this.child.getKey() + "";
    }

    public List<Object> getObjects() {
        return this.child.getObjects();
    }

    @Override
    public String toString() {
        return "BinaryParam(child=" + this.child + ")";
    }
}
