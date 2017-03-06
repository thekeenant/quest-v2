package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class AvgParam implements Param {
    private final Param expression;

    public AvgParam(Param expression) {
        this.expression = expression;
    }

    public AvgParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getKey() {
        return "AVG(" + this.expression.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.expression.getObjects();
    }

    @Override
    public String toString() {
        return "AvgParam(expression=" + this.expression + ")";
    }
}
