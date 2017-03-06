package net.avicus.quest.query;

import net.avicus.quest.Param;
import net.avicus.quest.parameter.ComparisonParam;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.ObjectParam;

/**
 * Something that can be where'd of course.
 * @param <C> The resulting object.
 */
public interface Filterable<C extends Query> {
    C where(Filter filter);

    default C where(String column, Object value) {
        return where(new FieldParam(column), value);
    }

    default C where(String column, Param value) {
        return where(new FieldParam(column), value);
    }

    default C where(String column, Object value, ComparisonParam comparison) {
        return where(new FieldParam(column), value, comparison);
    }

    default C where(String column, Param value, ComparisonParam comparison) {
        return where(new FieldParam(column), value, comparison);
    }

    default C where(Param column, Object value) {
        return where(column, new ObjectParam(value));
    }

    default C where(Param column, Param value) {
        return where(column, value, ComparisonParam.EQUAL);
    }

    default C where(Param column, Object value, ComparisonParam comparison) {
        return where(new Filter(column, new ObjectParam(value), comparison));
    }

    default C where(Param column, Param value, ComparisonParam comparison) {
        return where(new Filter(column, value, comparison));
    }
}
