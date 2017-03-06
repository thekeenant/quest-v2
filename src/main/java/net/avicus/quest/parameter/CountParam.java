package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class CountParam implements Param {
    /**
     * COUNT(*)
     */
    public static final CountParam WILDCARD = new CountParam(WildcardParam.INSTANCE);

    /**
     * COUNT(1)
     */
    public static final CountParam ONE = new CountParam(1);

    private final Param child;

    public CountParam(Param child) {
        this.child = child;
    }

    public CountParam(Object object) {
        this(new ObjectParam(object));
    }

    public CountParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getKey() {
        return "COUNT(" + this.child.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.child.getObjects();
    }

    @Override
    public String toString() {
        return "CountParam(child=" + this.child + ")";
    }
}
