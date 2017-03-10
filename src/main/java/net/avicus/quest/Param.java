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

    /**
     * Wrap this parameter in an ascending directional param.
     * @return The new directional param.
     */
    default DirectionalParam asc() {
        return new DirectionalParam(this, Direction.ASC);
    }

    /**
     * Wrap this parameter in an descending directional param.
     * @return The new directional param.
     */
    default DirectionalParam desc() {
        return new DirectionalParam(this, Direction.DESC);
    }

    /**
     * Wrap this parameter in a #{@link SumParam}.
     * @return The new param.
     */
    default SumParam sum() {
        return new SumParam(this);
    }

    /**
     * Wrap this parameter in a #{@link NotParam}.
     * @return The new param.
     */
    default NotParam not() {
        return new NotParam(this);
    }

    /**
     * Wrap this parameter in a #{@link MaxParam}.
     * @return The new param.
     */
    default MaxParam max() {
        return new MaxParam(this);
    }

    /**
     * Wrap this parameter in a #{@link MinParam}.
     * @return The new param.
     */
    default MinParam min() {
        return new MinParam(this);
    }

    /**
     * Wrap this parameter in a #{@link LowerParam}.
     * @return The new param.
     */
    default LowerParam lower() {
        return new LowerParam(this);
    }

    /**
     * Wrap this parameter in a #{@link UpperParam}.
     * @return The new param.
     */
    default UpperParam upper() {
        return new UpperParam(this);
    }

    /**
     * Wrap this parameter in a #{@link ModuloParam}.
     *
     * @param other The modulo divisor. (this mod other).
     * @return The new param.
     */
    default ModuloParam mod(Param other) {
        return new ModuloParam(this, other);
    }

    /**
     * Wrap this parameter in a #{@link ModuloParam}.
     *
     * @param other The modulo divisor. (this mod other).
     * @return The new param.
     */
    default ModuloParam mod(Object other) {
        return new ModuloParam(this, new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which matches another
     * param in a case insensitive manner (strings).
     *
     * i.e. "LOWER(this) == LOWER(other)"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter equalIgnoreCase(Param other) {
        return new Filter(new LowerParam(this), new LowerParam(other), ComparisonParam.EQUAL);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another
     * param in a case insensitive manner (strings).
     *
     * i.e. "LOWER(this) == LOWER(other)"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter equalIgnoreCase(Object other) {
        return equalIgnoreCase(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which inversely compares against another
     * param in a case insensitive manner (strings).
     *
     * i.e. "LOWER(this) != LOWER(other)"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter notEqualIgnoreCase(Param other) {
        return new Filter(new LowerParam(this), new LowerParam(other), ComparisonParam.NOT_EQUAL);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which inversely compares against another
     * param in a case insensitive manner (strings).
     *
     * i.e. "LOWER(this) != LOWER(other)"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter notEqualIgnoreCase(Object other) {
        return notEqualIgnoreCase(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another param.
     *
     * i.e. "this == other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter equal(Param other) {
        return new Filter(this, other);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another param.
     *
     * i.e. "this == other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter equal(Object other) {
        return equal(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another param.
     *
     * i.e. "this == other"
     *
     * Equivalent to #{@link this#equal(Param)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter eq(Param other) {
        return equal(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another param.
     *
     * i.e. "this == other"
     *
     * Equivalent to #{@link this#equal(Object)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter eq(Object other) {
        return equal(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another param,
     * both casted to BINARY.
     *
     * i.e. "CAST(this AS BINARY) == CAST(other AS BINARY)"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter binaryEqual(Param other) {
        return new Filter(new CastParam(this, CastType.BINARY), new CastParam(other, CastType.BINARY));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which compares against another param,
     * both casted to BINARY.
     *
     * i.e. "CAST(this AS BINARY) == CAST(other AS BINARY)"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter binaryEqual(Object other) {
        return binaryEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not equals comparison
     * against another param.
     *
     * i.e. "this != other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter notEqual(Param other) {
        return new Filter(this, other, ComparisonParam.NOT_EQUAL);
    }


    /**
     * Wrap this parameter in a #{@link Filter} which is a not equals comparison
     * against another param.
     *
     * i.e. "this != other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter notEqual(Object other) {
        return notEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not equals comparison
     * against another param.
     *
     * i.e. "this != other"
     *
     * Equivalent to #{@link this#notEqual(Param)}
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter ne(Param other) {
        return notEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not equals comparison
     * against another param.
     *
     * i.e. "this != other"
     *
     * Equivalent to #{@link this#notEqual(Object)}
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter ne(Object other) {
        return notEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than comparison
     * against another param.
     *
     * i.e. "this < other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter lessThan(Param other) {
        return new Filter(this, other, ComparisonParam.LESS_THAN);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than comparison
     * against another param.
     *
     * i.e. "this < other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter lessThan(Object other) {
        return lessThan(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than comparison
     * against another param.
     *
     * i.e. "this < other"
     *
     * Equivalent to #{@link this#lessThan(Param)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter lt(Param other) {
        return lessThan(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than comparison
     * against another param.
     *
     * i.e. "this < other"
     *
     * Equivalent to #{@link this#lessThan(Object)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter lt(Object other) {
        return lt(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than equals
     * comparison against another param.
     *
     * i.e. "this <= other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter lessOrEqual(Param other) {
        return new Filter(this, other, ComparisonParam.LESS_OR_EQUAL);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than equals
     * comparison against another param.
     *
     * i.e. "this <= other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter lessOrEqual(Object other) {
        return lessOrEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than equals
     * comparison against another param.
     *
     * i.e. "this <= other"
     *
     * Equivalent to #{@link this#lessOrEqual(Param)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter le(Param other) {
        return lessOrEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a not less than equals
     * comparison against another param.
     *
     * i.e. "this <= other"
     *
     * Equivalent to #{@link this#lessOrEqual(Object)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter le(Object other) {
        return lessOrEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than
     * comparison against another param.
     *
     * i.e. "this > other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter greaterThan(Param other) {
        return new Filter(this, other, ComparisonParam.GREATER_THAN);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than
     * comparison against another param.
     *
     * i.e. "this > other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter greaterThan(Object other) {
        return greaterThan(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than
     * comparison against another param.
     *
     * i.e. "this > other"
     *
     * Equivalent to #{@link this#greaterThan(Param)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter gt(Param other) {
        return greaterThan(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than
     * comparison against another param.
     *
     * i.e. "this > other"
     *
     * Equivalent to #{@link this#greaterThan(Object)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter gt(Object other) {
        return greaterThan(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than equals
     * comparison against another param.
     *
     * i.e. "this >= other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter greaterOrEqual(Param other) {
        return new Filter(this, other, ComparisonParam.GREATER_OR_EQUAL);
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than equals
     * comparison against another param.
     *
     * i.e. "this >= other"
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter greaterOrEqual(Object other) {
        return greaterOrEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than equals
     * comparison against another param.
     *
     * i.e. "this >= other"
     *
     * Equivalent to #{@link this#greaterOrEqual(Param)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter ge(Param other) {
        return greaterOrEqual(new ObjectParam(other));
    }

    /**
     * Wrap this parameter in a #{@link Filter} which is a greater than equals
     * comparison against another param.
     *
     * i.e. "this >= other"
     *
     * Equivalent to #{@link this#greaterOrEqual(Object)}.
     *
     * @param other The other param to match.
     * @return The new param.
     */
    default Filter ge(Object other) {
        return greaterOrEqual(other);
    }
}
