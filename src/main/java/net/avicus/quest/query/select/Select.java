package net.avicus.quest.query.select;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.Row;
import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.parameter.CustomParam;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.ObjectParam;
import net.avicus.quest.parameter.WildcardParam;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.Filterable;
import net.avicus.quest.query.Query;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Select implements Query<SelectResult, SelectConfig>, Filterable<Select>, Param {
    private final DatabaseConnection database;
    private final FieldParam table;
    private Filter filter;
    private List<Param> columns;
    private Param offset;
    private Param limit;
    private Param groupBy;
    private List<Param> order;
    private CustomParam join;

    public Select(DatabaseConnection database, FieldParam table) {
        this.database = database;
        this.table = table;
    }

    public Select duplicate() {
        Select copy = new Select(this.database, this.table);
        copy.filter = this.filter;
        copy.columns = this.columns == null ? null : new ArrayList<>(this.columns);
        copy.offset = this.offset;
        copy.limit = this.limit;
        copy.groupBy = this.groupBy;
        copy.order = this.order;
        copy.join = this.join;
        return copy;
    }

    public Select where(Filter filter) {
        if (this.filter != null) {
            return where(this.filter, filter);
        }
        else {
            Select select = duplicate();
            select.filter = filter;
            return select;
        }
    }

    public Select where(Filter filter, Filter and, Filter... ands) {
        // And the ands
        Filter result = filter.and(and);
        for (Filter additional : ands) {
            result = result.and(additional);
        }

        Select select = duplicate();
        select.filter = result;
        return select;
    }

    public Select select(List<Param> columns) {
        Select select = duplicate();
        select.columns = columns;
        return select;
    }

    public Select select(String... columns) {
        List<Param> parameters = new ArrayList<>();
        for (String column : columns) {
            parameters.add(new FieldParam(column));
        }
        return select(parameters);
    }

    public Select select(Param... columns) {
        return select(Arrays.asList(columns));
    }

    public Select groupBy(Param groupBy) {
        Select select = duplicate();
        select.groupBy = groupBy;
        return select;
    }

    public Select offset(int offset) {
        return offset(new ObjectParam(offset));
    }

    public Select offset(Param offset) {
        Select select = duplicate();
        select.offset = offset;
        return select;
    }

    public Select limit(int limit) {
        return limit(new ObjectParam(limit));
    }

    public Select limit(Param limit) {
        Select select = duplicate();
        select.limit = limit;
        return select;
    }

    public Select order(Param... order) {
        return order(Arrays.asList(order));
    }

    public Select order(List<Param> order) {
        Select select = duplicate();
        select.order = order;
        return select;
    }

    public Select join(CustomParam join) {
        Select select = duplicate();
        select.join = join;
        return select;
    }

    public Select join(String sql) {
        return join(new CustomParam(sql));
    }

    public Select join(String sql, Param... params) {
        return join(new CustomParam(sql, params));
    }

    public ParameterizedString build() {
        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("SELECT ");

        // Columns to select
        List<Param> columns = this.columns;
        if (columns == null || columns.isEmpty()) {
            columns = Collections.singletonList(WildcardParam.INSTANCE);
        }
        for (Param column : columns) {
            sb.append(column.getParamString());
            parameters.add(column);

            if (!columns.get(columns.size() - 1).equals(column)) {
                sb.append(", ");
            }
        }

        sb.append(" FROM ");

        sb.append(this.table.getParamString());
        parameters.add(this.table);

        if (this.join != null) {
            sb.append(" ");
            sb.append(this.join.getParamString());
            parameters.add(this.join);
        }

        if (this.filter != null) {
            sb.append(" WHERE ");
            ParameterizedString filterString = this.filter.build();
            sb.append(filterString.getSql());
            parameters.addAll(filterString.getParameters());
        }

        if (this.groupBy != null) {
            sb.append(" GROUP BY ");
            sb.append(this.groupBy.getParamString());
            parameters.add(this.groupBy);
        }

        if (this.order != null) {
            sb.append(" ORDER BY ");
            for (Param order : this.order) {
                sb.append(order.getParamString());
                parameters.add(order);
            }
        }

        if (this.limit != null) {
            sb.append(" LIMIT ");
            sb.append(this.limit.getParamString());
            parameters.add(this.limit);

            if (this.offset != null) {
                sb.append(" OFFSET ");
                sb.append(this.offset.getParamString());
                parameters.add(this.limit);
            }
        }

        return new ParameterizedString(sb.toString(), parameters);
    }

    @Override
    public SelectConfig getDefaultConfig() {
        return SelectConfig.DEFAULT;
    }

    @Override
    public SelectResult execute(SelectConfig config) throws DatabaseException {
        // The query
        ParameterizedString query = build();

        // Create statement
        PreparedStatement statement = config.createStatement(this.database, query.getSql());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return SelectResult.execute(statement);
    }

    public Stream<Row> stream(SelectConfig config) {
        return execute(config).stream();
    }

    public Stream<Row> stream() {
        return execute().stream();
    }

    @Override
    public String toString() {
        return "Select(" + build() + ")";
    }

    @Override
    public String getParamString() {
        return build().getSql();
    }

    @Override
    public List<Object> getValues() {
        List<Object> objects = new ArrayList<>();
        for (Param param : build().getParameters()) {
            objects.addAll(param.getValues());
        }
        return objects;
    }
}
