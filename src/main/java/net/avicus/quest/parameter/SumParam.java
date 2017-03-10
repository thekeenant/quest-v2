package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class SumParam implements Param {
    private final Param expression;

    public SumParam(Param expression) {
        this.expression = expression;
    }

    public SumParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getParamString() {
        return "SUM(" + this.expression.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.expression.getValues();
    }

    @Override
    public String toString() {
        return "SumParam(expression=" + this.expression + ")";
    }
}
