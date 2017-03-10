package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.ArrayList;
import java.util.List;

public class ModuloParam implements Param {
    private final Param dividend;
    private final Param divisor;
    private final List<Object> values;

    public ModuloParam(Param dividend, Param divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
        this.values = new ArrayList<>();
        this.values.addAll(dividend.getValues());
        this.values.addAll(divisor.getValues());
    }

    public ModuloParam(Param dividend, Object divisor) {
        this(dividend, new ObjectParam(divisor));
    }

    public ModuloParam(Object dividend, Object divisor) {
        this(new ObjectParam(dividend), new ObjectParam(divisor));
    }

    public ModuloParam(Object dividend, Param divisor) {
        this(new ObjectParam(dividend), divisor);
    }

    public ModuloParam(int dividend, int divisor) {
        this(new ObjectParam(dividend), new ObjectParam(divisor));
    }

    @Override
    public String getParamString() {
        return "(" + this.dividend.getParamString() + " % " + this.divisor.getParamString() + ")";
    }

    public List<Object> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        return "ModuloParam(dividend=" + this.dividend + ", divisor=" + this.divisor + ")";
    }
}
