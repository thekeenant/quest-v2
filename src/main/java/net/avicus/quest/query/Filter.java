package net.avicus.quest.query;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.parameter.ComparisonParam;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private final Param key;
    private final Param value;
    private final ComparisonParam comparison;
    private final List<Filter> ands;
    private final List<Filter> ors;
    private final boolean inverted;

    private Filter(Param key, Param value, ComparisonParam comparison, List<Filter> ands, List<Filter> ors, boolean inverted) {
        this.key = key;
        this.value = value;
        this.comparison = comparison;
        this.ands = ands;
        this.ors = ors;
        this.inverted = inverted;
    }

    public static Filter of(Param key, Param value, ComparisonParam comparison) {
        return new Filter(key, value, comparison, new ArrayList<>(), new ArrayList<>(), false);
    }

    public static Filter of(Param key, Param value) {
        return of(key, value, ComparisonParam.EQUAL);
    }

    public Filter duplicate() {
        List<Filter> ands = new ArrayList<>(this.ands);
        List<Filter> ors = new ArrayList<>(this.ors);
        return new Filter(key, value, comparison, ands, ors, inverted);
    }

    public Filter and(Filter filter) {
        Filter dup = duplicate();
        dup.ands.add(filter);
        return dup;
    }

    public Filter or(Filter filter) {
        Filter dup = duplicate();
        dup.ors.add(filter);
        return dup;
    }

    public Filter invert() {
        return new Filter(key, value, comparison, ands, ors, !inverted);
    }

    public ParameterizedString build() {
        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        if (inverted) {
            sb.append("NOT ");
        }
        sb.append("(");
        sb.append(this.key.getParamString());
        sb.append(this.comparison.getParamString());
        sb.append(this.value.getParamString());
        sb.append(")");

        parameters.add(this.key);
        parameters.add(this.comparison);
        parameters.add(this.value);

        if (!this.ands.isEmpty()) {
            sb.append(" AND ");
            if (this.ands.size() > 1) {
                sb.append("(");
            }
            for (Filter filter : this.ands) {
                ParameterizedString built = filter.build();
                sb.append(built.getSql());
                parameters.addAll(built.getParameters());

                if (!this.ands.get(this.ands.size() - 1).equals(filter)) {
                    sb.append(" AND ");
                }
            }
            if (this.ands.size() > 1) {
                sb.append(")");
            }
        }

        if (!this.ors.isEmpty()) {
            sb.append(" OR ");
            if (this.ors.size() > 1) {
                sb.append("(");
            }
            for (Filter filter : this.ors) {
                ParameterizedString built = filter.build();
                sb.append(built.getSql());
                parameters.addAll(built.getParameters());

                if (!this.ors.get(this.ors.size() - 1).equals(filter)) {
                    sb.append(" OR ");
                }
            }
            if (this.ors.size() > 1) {
                sb.append(")");
            }
        }

        return new ParameterizedString(sb.toString(), parameters);
    }
}
