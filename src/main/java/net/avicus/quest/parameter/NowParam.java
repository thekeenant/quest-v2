package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Collections;
import java.util.List;

public class NowParam implements Param {
    public static final NowParam INSTANCE = new NowParam();

    private NowParam() {

    }

    @Override
    public String getParamString() {
        return "NOW()";
    }

    public List<Object> getValues() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "NowParam()";
    }
}
