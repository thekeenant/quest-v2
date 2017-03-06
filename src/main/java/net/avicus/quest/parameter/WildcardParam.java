package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Collections;
import java.util.List;

public class WildcardParam implements Param {
    public static final WildcardParam INSTANCE = new WildcardParam();

    private WildcardParam() {

    }

    @Override
    public String getKey() {
        return "*";
    }

    public List<Object> getObjects() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "WildcardParam()";
    }
}
