package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Collections;
import java.util.List;

public class ObjectParam implements Param {
    private final Object value;

    public ObjectParam(Object value) {
        this.value = value;
    }

    @Override
    public String getParamString() {
        return "?";
    }

    public List<Object> getValues() {
        return Collections.singletonList(this.value);
    }

    @Override
    public String toString() {
        return "ObjectParam(values=" + getValues() + ")";
    }
}
