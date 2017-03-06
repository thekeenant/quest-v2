package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class NotParam implements Param {
    private final Param child;

    public NotParam(Param child) {
        this.child = child;
    }

    @Override
    public String getKey() {
        return "NOT " + this.child.getKey();
    }

    @Override
    public List<Object> getObjects() {
        return this.child.getObjects();
    }

    @Override
    public String toString() {
        return "NotParam(child=" + this.child + ")";
    }
}
