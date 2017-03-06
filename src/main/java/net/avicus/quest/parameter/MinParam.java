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
    public String getKey() {
        return "MIN(" + this.expression.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.expression.getObjects();
    }

    @Override
    public String toString() {
        return "MinParam(expression=" + this.expression + ")";
    }
}
