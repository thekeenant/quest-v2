package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class MinParam implements Param {
    private final Param expression;

    public MinParam(Param expression) {
        this.expression = expression;
    }

    public MinParam(String column) {
        this(new FieldParam(column));
    }

    @Override
    public String getParamString() {
        return "MIN(" + this.expression.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.expression.getValues();
    }

    @Override
    public String toString() {
        return "MinParam(expression=" + this.expression + ")";
    }
}
