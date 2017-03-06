package net.avicus.quest.query;

import net.avicus.quest.Param;
import net.avicus.quest.ParamString;
import net.avicus.quest.parameter.ComparisonParam;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.ObjectParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filter {
    private final Param key;
    private final Param value;
    private final ComparisonParam comparison;
    private final List<Filter> ands;
    private final List<Filter> ors;

    private Filter(Param key, Param value, ComparisonParam comparison, List<Filter> ands, List<Filter> ors) {
        this.key = key;
        this.value = value;
        this.comparison = comparison;
        this.ands = ands;
        this.ors = ors;
    }

    public Filter(Param key, Param value, ComparisonParam comparison) {
        this(key, value, comparison, Collections.emptyList(), Collections.emptyList());
    }

    public Filter(Param key, Param value) {
        this(key, value, ComparisonParam.EQUAL, Collections.emptyList(), Collections.emptyList());
    }

    public Filter(Param key, Object value, ComparisonParam comparison) {
        this(key, new ObjectParam(value), comparison);
    }

    public Filter(Param key, Object value) {
        this(key, new ObjectParam(value));
    }

    public Filter(String field, Object value, ComparisonParam comparison) {
        this(new FieldParam(field), new ObjectParam(value), comparison);
    }

    public Filter(String field, Object value) {
        this(new FieldParam(field), new ObjectParam(value));
    }

    public Filter(String field, Param value, ComparisonParam comparison) {
        this(new FieldParam(field), value, comparison);
    }

    public Filter(String field, Param value) {
        this(new FieldParam(field), value);
    }

    public Filter duplicate() {
        Filter filter = new Filter(this.key, this.value, this.comparison);
        filter.ands.addAll(this.ands);
        filter.ors.addAll(this.ors);
        return filter;
    }

    public Filter and(Filter filter) {
        return duplicate().and(filter);
    }

    public Filter or(Filter filter) {
        return duplicate().or(filter);
    }

    public ParamString build() {
        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("(");
        sb.append(this.key.getKey());
        sb.append(this.comparison.getKey());
        sb.append(this.value.getKey());
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
                ParamString built = filter.build();
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
                ParamString built = filter.build();
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

        return new ParamString(sb.toString(), parameters);
    }
}
