package net.avicus.quest;

import net.avicus.quest.parameter.*;
import net.avicus.quest.parameter.CastParam.CastType;
import net.avicus.quest.parameter.DirectionalParam.Direction;
import net.avicus.quest.query.Filter;

import java.util.List;

public interface Param {
    /**
     * Get this parameter's raw parameterized SQL string.
     *
     * It should contain the same number of "?" symbols as the number
     * of elements returned by #{@link this#getValues()}.
     *
     * @return The SQL string.
     */
    String getParamString();

    /**
     * Get this parameter's values which will replace the "?" symbols
     * in the string returned by #{@link this#getParamString()}
     * @return
     */
    List<Object> getValues();

    default DirectionalParam asc() {
        return new DirectionalParam(this, Direction.ASC);
    }

    default DirectionalParam desc() {
        return new DirectionalParam(this, Direction.DESC);
    }

    default SumParam sum() {
        return new SumParam(this);
    }

    default NotParam not() {
        return new NotParam(this);
    }

    default MaxParam max() {
        return new MaxParam(this);
    }

    default MinParam min() {
        return new MinParam(this);
    }

    default LowerParam lower() {
        return new LowerParam(this);
    }

    default UpperParam upper() {
        return new UpperParam(this);
    }

    default ModuloParam mod(Param other) {
        return new ModuloParam(this, other);
    }

    default ModuloParam mod(Object other) {
        return mod(new ObjectParam(other));
    }

    default Filter like(Param other) {
        return Filter.of(this, other, ComparisonParam.LIKE);
    }

    default Filter like(Object other) {
        return like(new ObjectParam(other));
    }

    default Filter equalIgnoreCase(Param other) {
        return Filter.of(lower(), lower(), ComparisonParam.EQUAL);
    }

    default Filter equalIgnoreCase(Object other) {
        return equalIgnoreCase(new ObjectParam(other));
    }

    default Filter equal(Param other) {
        return Filter.of(this, other);
    }

    default Filter equal(Object other) {
        return equal(new ObjectParam(other));
    }

    default Filter eq(Param other) {
        return equal(other);
    }

    default Filter eq(Object other) {
        return equal(other);
    }

    default Filter binaryEqual(Param other) {
        return Filter.of(new CastParam(this, CastType.BINARY), new CastParam(other, CastType.BINARY));
    }

    default Filter binaryEqual(Object other) {
        return binaryEqual(new ObjectParam(other));
    }

    default Filter notEqual(Param other) {
        return Filter.of(this, other, ComparisonParam.NOT_EQUAL);
    }

    default Filter notEqual(Object other) {
        return notEqual(new ObjectParam(other));
    }

    default Filter ne(Param other) {
        return notEqual(other);
    }

    default Filter ne(Object other) {
        return notEqual(other);
    }

    default Filter lessThan(Param other) {
        return Filter.of(this, other, ComparisonParam.LESS_THAN);
    }

    default Filter lessThan(Object other) {
        return lessThan(new ObjectParam(other));
    }

    default Filter lt(Param other) {
        return lessThan(other);
    }

    default Filter lt(Object other) {
        return lessThan(other);
    }

    default Filter lessOrEqual(Param other) {
        return Filter.of(this, other, ComparisonParam.LESS_OR_EQUAL);
    }

    default Filter lessOrEqual(Object other) {
        return lessOrEqual(new ObjectParam(other));
    }

    default Filter le(Param other) {
        return lessOrEqual(other);
    }

    default Filter le(Object other) {
        return lessOrEqual(other);
    }

    default Filter greaterThan(Param other) {
        return Filter.of(this, other, ComparisonParam.GREATER_THAN);
    }

    default Filter greaterThan(Object other) {
        return greaterThan(new ObjectParam(other));
    }

    default Filter gt(Param other) {
        return greaterThan(other);
    }

    default Filter gt(Object other) {
        return greaterThan(other);
    }

    default Filter greaterOrEqual(Param other) {
        return Filter.of(this, other, ComparisonParam.GREATER_OR_EQUAL);
    }

    default Filter greaterOrEqual(Object other) {
        return greaterOrEqual(new ObjectParam(other));
    }

    default Filter ge(Param other) {
        return greaterOrEqual(other);
    }

    default Filter ge(Object other) {
        return greaterOrEqual(other);
    }
}
