package net.avicus.quest;

import net.avicus.quest.parameter.AvgParam;
import net.avicus.quest.parameter.CountParam;
import net.avicus.quest.parameter.WildcardParam;

/**
 * Todo: Do all the functions
 */
public class Functions {
    public static AvgParam avg(Param param) {
        return new AvgParam(param);
    }

    public static AvgParam avg(String exp) {
        return new AvgParam(exp);
    }

    public static CountParam count() {
        return count(wildcard());
    }

    public static CountParam count(Param param) {
        return new CountParam(param);
    }

    public static CountParam count(String column) {
        return new CountParam(column);
    }

    public static WildcardParam wildcard() {
        return WildcardParam.INSTANCE;
    }
}
