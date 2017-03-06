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
        this.values.addAll(dividend.getObjects());
        this.values.addAll(divisor.getObjects());
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
    public String getKey() {
        return "(" + this.dividend.getKey() + " % " + this.divisor.getKey() + ")";
    }

    public List<Object> getObjects() {
        return this.values;
    }

    @Override
    public String toString() {
        return "ModuloParam(dividend=" + this.dividend + ", divisor=" + this.divisor + ")";
    }
}
