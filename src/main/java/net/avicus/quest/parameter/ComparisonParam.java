package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.Collections;
import java.util.List;

public class ComparisonParam implements Param {
    public static ComparisonParam EQUAL = new ComparisonParam(" = ");
    public static ComparisonParam NOT_EQUAL = new ComparisonParam(" != "); // same as "<>"
    public static ComparisonParam LESS_THAN = new ComparisonParam(" < ");
    public static ComparisonParam LESS_OR_EQUAL = new ComparisonParam(" <= ");
    public static ComparisonParam NOT_LESS_THAN = new ComparisonParam(" !< ");
    public static ComparisonParam GREATER_THAN = new ComparisonParam(" > ");
    public static ComparisonParam GREATER_OR_EQUAL = new ComparisonParam(" >= ");
    public static ComparisonParam NOT_GREATER_THAN = new ComparisonParam(" !> ");
    public static ComparisonParam IN = new ComparisonParam(" IN ");
    public static ComparisonParam LIKE = new ComparisonParam(" LIKE ");
    public static ComparisonParam BETWEEN = new ComparisonParam(" BETWEEN ");
    public static ComparisonParam IS = new ComparisonParam(" IS ");

    private final String symbol;

    public ComparisonParam(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getKey() {
        return this.symbol;
    }

    @Override
    public List<Object> getObjects() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "ComparisonParam(symbol='" + this.symbol + "')";
    }
}
