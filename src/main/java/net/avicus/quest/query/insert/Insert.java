package net.avicus.quest.query.insert;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.Query;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.select.Select;

import java.sql.PreparedStatement;
import java.util.*;

public class Insert implements Query<InsertResult, InsertConfig> {
    private final DatabaseConnection database;
    private final FieldParam table;
    private final List<Insertion> insertions;
    private Select select;

    public Insert(DatabaseConnection database, FieldParam table) {
        this.database = database;
        this.table = table;
        this.insertions = new ArrayList<>();
    }

    public Insert duplicate() {
        Insert copy = new Insert(this.database, this.table);
        copy.insertions.addAll(this.insertions);
        copy.select = select;
        return copy;
    }

    public Insert insert(Insertion insertion) {
        if (this.select != null) {
            throw new DatabaseException("Select query cannot be combined insert insertion values.");
        }
        Insert query = duplicate();
        query.insertions.add(insertion);
        return query;
    }

    public Insert select(Select select) {
        if (!this.insertions.isEmpty()) {
            throw new DatabaseException("Insertion values cannot be combined insert select query.");
        }
        Insert query = duplicate();
        query.select = select;
        return query;
    }

    public ParameterizedString build() {
        if (this.insertions.isEmpty()) {
            throw new DatabaseException("No insertions to be made.");
        }

        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("INSERT INTO ");

        sb.append(this.table.getParamString());
        parameters.add(this.table);

        sb.append(" ");
        if (this.select == null) {
            sb.append("(");
            Set<String> columns = new HashSet<>();
            for (Insertion insertion : this.insertions) {
                for (String column : insertion.getColumns()) {
                    if (!columns.contains(column)) {
                        columns.add(column);
                        sb.append("`").append(column).append("`");
                        sb.append(", ");
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
            sb.append(")");

            sb.append(" VALUES ");
            for (Insertion insertion : this.insertions) {
                sb.append("(");
                for (String column : columns) {
                    Param value = insertion.getValue(column);
                    sb.append(value.getParamString());
                    parameters.add(value);
                    sb.append(", ");
                }
                sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
                sb.append(")");
            }
        }
        else {
            sb.append(this.select.getParamString());
            parameters.add(this.select);
        }

        return new ParameterizedString(sb.toString(), parameters);
    }

    @Override
    public InsertResult execute(InsertConfig config) throws DatabaseException {
        // The query
        ParameterizedString query = build();

        // Create statement
        PreparedStatement statement = config.createStatement(this.database, query.getSql());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return InsertResult.execute(statement);
    }

    @Override
    public String toString() {
        return "Insert(" + build() + ")";
    }

    @Override
    public InsertConfig getDefaultConfig() {
        return InsertConfig.DEFAULT;
    }
}
