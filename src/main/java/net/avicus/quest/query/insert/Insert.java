package net.avicus.quest.query.insert;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.Record;
import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.query.select.Select;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Insert {
    private final DatabaseConnection database;
    private final FieldParam table;
    private final List<Record> insertions;
    private Select selectQuery;

    public Insert(DatabaseConnection database, FieldParam table) {
        this.database = database;
        this.table = table;
        this.insertions = new ArrayList<>();
    }

    public Insert duplicate() {
        Insert copy = new Insert(this.database, this.table);
        copy.insertions.addAll(this.insertions);
        copy.selectQuery = selectQuery;
        return copy;
    }

    public Insert with(Record... insertions) {
        return with(Arrays.asList(insertions));
    }

    public Insert with(List<Record> insertions) {
        if (this.selectQuery != null) {
            throw new DatabaseException("Select query cannot be combined with insertions");
        }
        Insert query = duplicate();
        query.insertions.addAll(insertions);
        return query;
    }

    public Insert select(Select select) {
        if (!this.insertions.isEmpty()) {
            throw new DatabaseException("Insertions cannot be combined with a select insert");
        }
        Insert query = duplicate();
        query.selectQuery = select;
        return query;
    }

    public ParameterizedString build() {
        if (this.insertions.isEmpty() && selectQuery == null) {
            throw new DatabaseException("No insertions to be made.");
        }

        StringBuilder sb = new StringBuilder();
        List<Param> parameters = new ArrayList<>();

        sb.append("INSERT INTO ");

        sb.append(this.table.getParamString());
        parameters.add(this.table);

        // TODO
//        sb.append(" ");
//        if (this.selectQuery == null) {
//            sb.append("(");
//            Set<String> columns = new HashSet<>();
//            for (Record insertion : this.insertions) {
//                for (int i = 1; i <= insertion.getColumnCount(); i++) {
//                    String column = insertion.getColumnName(i);
//                    if (!columns.contains(column)) {
//                        columns.add(column);
//                        sb.append("`").append(column).append("`");
//                        sb.append(", ");
//                    }
//                }
//            }
//            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
//            sb.append(")");
//
//            sb.append(" VALUES ");
//            for (Record insertion : insertions) {
//                sb.append("(");
//                for (String column : columns) {
//                    Object data = insertion.field(column).asObject().orElse(null);
//                    Param param = data == null ? NullParam.INSTANCE : new ObjectParam(data);
//                    sb.append(param.getParamString());
//                    parameters.add(param);
//                    sb.append(", ");
//                }
//                // remove last comma
//                sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
//                sb.append("), ");
//            }
//
//            // remove last comma
//            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
//        }
//        else {
//            // INSERT INTO something (___, ___, ___) ...
//            if (!selectQuery.getColumns().isEmpty()) {
//                sb.append(" (");
//                for (Param param : selectQuery.getColumns()) {
//                    sb.append(param.getParamString());
//                    sb.append(", ");
//                }
//                sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
//                parameters.addAll(selectQuery.getColumns());
//                sb.append(") ");
//            }
//            Param param = selectQuery.toParam();
//            sb.append(param.getParamString());
//            parameters.add(param);
//        }

        return new ParameterizedString(sb.toString(), parameters);
    }

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
