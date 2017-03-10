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
    public String getParamString() {
        return "AVG(" + this.expression.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.expression.getValues();
    }

    @Override
    public String toString() {
        return "AvgParam(expression=" + this.expression + ")";
    }
}
