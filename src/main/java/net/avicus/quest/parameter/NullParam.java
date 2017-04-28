package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NullParam implements Param {
    public static final NullParam INSTANCE = new NullParam();
    private static final List<Object> VALUES = Collections.singletonList(NULL);

    private NullParam() {

    }

    @Override
    public String getParamString() {
        return "?";
    }

    public List<Object> getValues() {
        return VALUES;
    }

    @Override
    public String toString() {
        return "NullParam()";
    }
}
