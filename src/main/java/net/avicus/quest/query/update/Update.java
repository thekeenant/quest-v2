package net.avicus.quest.query.update;

import net.avicus.quest.Param;
import net.avicus.quest.ParamString;
import net.avicus.quest.parameter.DirectionalParam;
import net.avicus.quest.parameter.ObjectParam;
import net.avicus.quest.query.Query;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.Filterable;
import net.avicus.quest.parameter.FieldParam;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.Map.Entry;

public class Update implements Query<UpdateResult, UpdateConfig>, Filterable<Update> {
    private final Database database;
    private final FieldParam table;
    private final Map<String, Param> changes;
    private Filter filter;
    private Param limit;
    private List<DirectionalParam> order;

    public Update(Database database, FieldParam table) {
        this.database = database;
        this.table = table;
        this.changes = new HashMap<>();
    }

    public Update duplicate() {
        Update copy = new Update(this.database, this.table);
        copy.changes.putAll(this.changes);
        copy.filter = this.filter;
        copy.limit = this.limit;
        copy.order = this.order;
        return copy;
    }

    public Update where(Filter filter) {
        if (this.filter != null) {
            return where(this.filter, filter);
        }
        else {
            Update select = duplicate();
            select.filter = filter;
            return select;
        }
    }

    public Update where(Filter filter, Filter and, Filter... ands) {
        // And the ands
        Filter result = filter.and(and);
        for (Filter additional : ands) {
            result = result.and(additional);
        }

        Update query = duplicate();
        query.filter = result;
        return query;
    }

    public Update set(String column, Param value) {
        Update query = duplicate();
        query.changes.put(column, value);
        return query;
    }

    public Update set(String column, Object value) {
        return set(column, new ObjectParam(value));
    }

    public Update limit(int limit) {
        return limit(new ObjectParam(limit));
    }

    public Update limit(Param limit) {
        Update update = duplicate();
        update.limit = limit;
        return update;
    }

    public Update order(DirectionalParam... order) {
        return order(Arrays.asList(order));
    }

    public Update order(List<DirectionalParam> order) {
        Update update = duplicate();
        update.order = order;
        return update;
    }

    public ParamString build() {
        if (this.changes.isEmpty()) {
            throw new DatabaseException("No changes to be made.");
        }

        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("UPDATE ");

        sb.append(this.table.getKey());
        parameters.add(this.table);

        sb.append(" SET ");

        for (Entry<String, Param> entry : this.changes.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" = ");
            sb.append(entry.getValue().getKey());
            sb.append(", ");
            parameters.add(entry.getValue());
        }

        // Remove last comma separator
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);

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
    public UpdateResult execute(Optional<UpdateConfig> config) throws DatabaseException {
        // The query
        ParamString query = build();

        // Create statement
        PreparedStatement statement = config.orElse(UpdateConfig.DEFAULT).createStatement(this.database, query.getSql());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return UpdateResult.execute(statement);
    }

    @Override
    public String toString() {
        return "Update(" + build() + ")";
    }
}
