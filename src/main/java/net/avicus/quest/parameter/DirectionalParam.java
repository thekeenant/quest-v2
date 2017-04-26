package net.avicus.quest.parameter;

import net.avicus.quest.Param;

import java.util.List;

public class DirectionalParam implements Param {
    private final Param child;
    private final Direction direction;

    public DirectionalParam(String column) {
        this(new FieldParam(column));
    }

    public DirectionalParam(String column, Direction direction) {
        this(new FieldParam(column), direction);
    }

    public DirectionalParam(Param child) {
        this(child, Direction.ASC);
    }

    public DirectionalParam(Param child, Direction direction) {
        this.child = child;
        this.direction = direction;
    }

    public DirectionalParam(int field, Direction direction) {
        this(new ObjectParam(field), direction);
    }

    public DirectionalParam(int field) {
        this(new ObjectParam(field));
    }

    @Override
    public String getParamString() {
        return this.child.getParamString() + " " + this.direction.name();
    }

    public List<Object> getValues() {
        return this.child.getValues();
    }

    public enum Direction {
        /**
         * Ascending
         */
        ASC,

        /**
         * Descending
         */
        DESC
    }

    @Override
    public String toString() {
        return "DirectionalParam(child=" + this.child + ", direction=" + this.direction + ")";
    }
}
