package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Collections;
import java.util.List;

public class NullParam implements Param {
    public static final NullParam INSTANCE = new NullParam();

    private NullParam() {

    }

    @Override
    public String getKey() {
        return "NULL";
    }

    public List<Object> getObjects() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "NullParam()";
    }
}
