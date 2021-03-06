package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Collections;
import java.util.List;

public class DefaultParam implements Param {
    public static final DefaultParam INSTANCE = new DefaultParam();

    private DefaultParam() {

    }

    @Override
    public String getParamString() {
        return "DEFAULT";
    }

    public List<Object> getValues() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "DefaultParam()";
    }
}
