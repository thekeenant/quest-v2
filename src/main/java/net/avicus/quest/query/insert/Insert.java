package net.avicus.quest.query.insert;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.Row;
import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.NullParam;
import net.avicus.quest.parameter.ObjectParam;
import net.avicus.quest.query.Query;
import net.avicus.quest.query.select.Select;

import java.sql.PreparedStatement;
import java.util.*;

public class Insert implements Query<InsertResult> {
    private final DatabaseConnection database;
    private final FieldParam table;
    private final List<Row> insertions;
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

    public Insert insert(Row... insertions) {
        if (this.select != null) {
            throw new DatabaseException("Select query cannot be combined with insertions");
        }
        Insert query = duplicate();
        query.insertions.addAll(Arrays.asList(insertions));
        return query;
    }

    public Insert select(Select select) {
        if (!this.insertions.isEmpty()) {
            throw new DatabaseException("Insertions cannot be combined with a select insert");
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
            for (Row insertion : this.insertions) {
                for (int i = 1; i <= insertion.getColumnCount(); i++) {
                    String column = insertion.getColumnName(i);
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
            for (Row insertion : insertions) {
                sb.append("(");
                for (String column : columns) {
                    Object data = insertion.get(column).asObject().orElse(null);
                    Param param = data == null ? NullParam.INSTANCE : new ObjectParam(data);
                    sb.append(param.getParamString());
                    parameters.add(param);
                    sb.append(", ");
                }
                // remove last comma
                sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
                sb.append("), ");
            }

            // remove last comma
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        }
        else {
            Param param = select.toParam();
            sb.append(param.getParamString());
            parameters.add(param);
        }

        return new ParameterizedString(sb.toString(), parameters);
    }

    @Override
    public InsertResult execute() throws DatabaseException {
        // The query
        ParameterizedString query = build();

        // Create statement
        PreparedStatement statement = database.createUpdateStatement(query.getSql());

        // Add variables (?, ?)
        query.apply(statement, 1);

        return InsertResult.execute(statement);
    }

    @Override
    public String toString() {
        return "Insert(" + build() + ")";
    }
}
