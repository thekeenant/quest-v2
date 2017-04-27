package net.avicus.quest.query.delete;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.parameter.DirectionalParam;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.ObjectParam;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.Filterable;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Delete implements Filterable<Delete> {
    private final DatabaseConnection database;
    private final FieldParam table;
    private Filter filter;
    private Param limit;
    private List<DirectionalParam> order;

    public Delete(DatabaseConnection database, FieldParam table) {
        this.database = database;
        this.table = table;
    }

    public Delete duplicate() {
        Delete copy = new Delete(this.database, this.table);
        copy.filter = this.filter;
        copy.limit = this.limit;
        copy.order = this.order;
        return copy;
    }

    public Delete where(Filter filter) {
        if (this.filter != null) {
            return where(this.filter, filter);
        }
        else {
            Delete select = duplicate();
            select.filter = filter;
            return select;
        }
    }

    public Delete where(Filter filter, Filter and, Filter... ands) {
        // And the ands
        Filter result = filter.and(and);
        for (Filter additional : ands) {
            result = result.and(additional);
        }

        Delete query = duplicate();
        query.filter = result;
        return query;
    }

    public Delete limit(int limit) {
        return limit(new ObjectParam(limit));
    }

    public Delete limit(Param limit) {
        Delete update = duplicate();
        update.limit = limit;
        return update;
    }

    public Delete order(DirectionalParam... order) {
        return order(Arrays.asList(order));
    }

    public Delete order(List<DirectionalParam> order) {
        Delete update = duplicate();
        update.order = order;
        return update;
    }

    public ParameterizedString build() {
        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("DELETE FROM ");

        sb.append(this.table.getParamString());
        parameters.add(this.table);

        if (this.filter != null) {
            sb.append(" WHERE ");
            ParameterizedString filterString = this.filter.build();
            sb.append(filterString.getSql());
            parameters.addAll(filterString.getParameters());
        }

        if (this.order != null) {
            sb.append(" ORDER BY ");
            for (DirectionalParam order : this.order) {
                sb.append(order.getParamString());
                parameters.add(order);
            }
        }

        if (this.limit != null) {
            sb.append(" LIMIT ");
            sb.append(this.limit.getParamString());
            parameters.add(this.limit);
        }

        return new ParameterizedString(sb.toString(), parameters);
    }

    public DeleteResult execute() throws DatabaseException {
        // The query
        ParameterizedString query = build();

        // Create statement
        PreparedStatement statement = database.createUpdateStatement(query.getSql());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return DeleteResult.execute(statement);
    }

    @Override
    public String toString() {
        return "Delete(" + build() + ")";
    }
}
