package net.avicus.quest.query.select;

import net.avicus.quest.Param;
import net.avicus.quest.ParamString;
import net.avicus.quest.query.Query;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.Filterable;
import net.avicus.quest.parameter.*;

import java.sql.PreparedStatement;
import java.util.*;

public class Select implements Query<SelectResult, SelectConfig>, Filterable<Select>, Param {
    private final Database database;
    private final FieldParam table;
    private Filter filter;
    private List<Param> columns;
    private Param offset;
    private Param limit;
    private Param groupBy;
    private List<Param> order;
    private CustomParam join;

    public Select(Database database, FieldParam table) {
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

    public ParamString build() {
        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("SELECT ");

        // Columns to select
        List<Param> columns = this.columns;
        if (columns == null || columns.isEmpty()) {
            columns = Collections.singletonList(WildcardParam.INSTANCE);
        }
        for (Param column : columns) {
            sb.append(column.getKey());
            parameters.add(column);

            if (!columns.get(columns.size() - 1).equals(column)) {
                sb.append(", ");
            }
        }

        sb.append(" FROM ");

        sb.append(this.table.getKey());
        parameters.add(this.table);

        if (this.join != null) {
            sb.append(" ");
            sb.append(this.join.getKey());
            parameters.add(this.join);
        }

        if (this.filter != null) {
            sb.append(" WHERE ");
            ParamString filterString = this.filter.build();
            sb.append(filterString.getSql());
            parameters.addAll(filterString.getParameters());
        }

        if (this.groupBy != null) {
            sb.append(" GROUP BY ");
            sb.append(this.groupBy.getKey());
            parameters.add(this.groupBy);
        }

        if (this.order != null) {
            sb.append(" ORDER BY ");
            for (Param order : this.order) {
                sb.append(order.getKey());
                parameters.add(order);
            }
        }

        if (this.limit != null) {
            sb.append(" LIMIT ");
            sb.append(this.limit.getKey());
            parameters.add(this.limit);

            if (this.offset != null) {
                sb.append(" OFFSET ");
                sb.append(this.offset.getKey());
                parameters.add(this.limit);
            }
        }

        return new ParamString(sb.toString(), parameters);
    }

    @Override
    public SelectResult execute(Optional<SelectConfig> config) throws DatabaseException {
        // The query
        ParamString query = build();

        // Create statement
        PreparedStatement statement = config.orElse(SelectConfig.DEFAULT).createStatement(this.database, query.getSql());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return SelectResult.execute(statement);
    }

    @Override
    public String toString() {
        return "Select(" + build() + ")";
    }

    @Override
    public String getKey() {
        return build().getSql();
    }

    @Override
    public List<Object> getObjects() {
        List<Object> objects = new ArrayList<>();
        for (Param param : build().getParameters()) {
            objects.addAll(param.getObjects());
        }
        return objects;
    }
}
