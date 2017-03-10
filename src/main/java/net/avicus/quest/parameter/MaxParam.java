package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class MaxParam implements Param {
    private final Param expression;

    public MaxParam(Param expression) {
        this.expression = expression;
    }

    public MaxParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getParamString() {
        return "MAX(" + this.expression.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.expression.getValues();
    }

    @Override
    public String toString() {
        return "MaxParam(expression=" + this.expression + ")";
    }
}
