package net.avicus.quest.query.delete;

import net.avicus.quest.Param;
import net.avicus.quest.ParamString;
import net.avicus.quest.parameter.DirectionalParam;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.Query;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.Filterable;
import net.avicus.quest.parameter.ObjectParam;

import java.sql.PreparedStatement;
import java.util.*;

public class Delete implements Query<DeleteResult, DeleteConfig>, Filterable<Delete> {
    private final Database database;
    private final FieldParam table;
    private Filter filter;
    private Param limit;
    private List<DirectionalParam> order;

    public Delete(Database database, FieldParam table) {
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

    public ParamString build() {
        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("DELETE FROM ");

        sb.append(this.table.getKey());
        parameters.add(this.table);

        if (this.filter != null) {
            sb.append(" WHERE ");
            ParamString filterString = this.filter.build();
            sb.append(filterString.getSql());
            parameters.addAll(filterString.getParameters());
        }

        if (this.order != null) {
            sb.append(" ORDER BY ");
            for (DirectionalParam order : this.order) {
                sb.append(order.getKey());
                parameters.add(order);
            }
        }

        if (this.limit != null) {
            sb.append(" LIMIT ");
            sb.append(this.limit.getKey());
            parameters.add(this.limit);
        }

        return new ParamString(sb.toString(), parameters);
    }

    @Override
    public DeleteResult execute(Optional<DeleteConfig> config) throws DatabaseException {
        // The query
        ParamString query = build();

        // Create statement
        PreparedStatement statement = config.orElse(DeleteConfig.DEFAULT).createStatement(this.database, query.getSql());


        System.out.println(build());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return DeleteResult.execute(statement);
    }

    @Override
    public String toString() {
        return "Delete(" + build() + ")";
    }
}
