package net.avicus.quest.query.insert;

import net.avicus.quest.Column;
import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.database.DatabaseConnection;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.parameter.FieldParam;
import net.avicus.quest.parameter.ObjectParam;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class InsertRecords {
    private final DatabaseConnection database;
    private final FieldParam table;

    private final List<Map<String, Param>> insertions;

    public InsertRecords(DatabaseConnection database, FieldParam table) {
        this.database = database;
        this.table = table;
        insertions = new ArrayList<>();
    }

    private InsertRecords duplicate() {
        InsertRecords copy = new InsertRecords(database, table);
        for (Map<String, Param> insertion : insertions) {
            copy.insertions.add(new HashMap<>(insertion));
        }
        return copy;
    }

    private Map<String, Param> currentRecord() {
        if (insertions.size() == 0) {
            insertions.add(new HashMap<>());
        }
        return insertions.get(insertions.size() - 1);
    }

    public InsertRecords newRecord() {
        if (currentRecord().size() == 0) {
            throw new IllegalStateException("Record empty, cannot proceed to a new record");
        }
        InsertRecords copy = duplicate();
        copy.insertions.add(new HashMap<>());
        return copy;
    }

    public <T> InsertRecords with(Column<T> column, T value) {
        return with(column.getName(), column.wrapObject(value));
    }

    public InsertRecords with(String column, Object value) {
        return with(column, new ObjectParam(value));
    }

    public InsertRecords with(String column, Param value) {
        InsertRecords copy = duplicate();
        copy.currentRecord().put(column, value);
        return copy;
    }

    private Set<String> allColumns() {
        Set<String> columns = new HashSet<>();
        for (Map<String, Param> insertion : insertions) {
            columns.addAll(insertion.keySet());
        }
        return columns;
    }

    public ParameterizedString build() {
        StringBuilder sql = new StringBuilder();
        List<Param> params = new ArrayList<>();

        sql.append("INSERT INTO ");

        sql.append(table.getParamString());
        params.add(table);

        Set<String> columns = allColumns();

        sql.append(" (");
        for (String column : columns) {
            FieldParam param = new FieldParam(column);

            sql.append(param.getParamString());
            sql.append(",");
            params.add(param);
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") VALUES ");

        for (Map<String, Param> insertion : insertions) {
            sql.append("(");
            for (String column : columns) {
                Param param = insertion.getOrDefault(column, Param.NULL);

                sql.append(param.getParamString());
                sql.append(",");
                params.add(param);
            }
            sql.deleteCharAt(sql.length() - 1);

            sql.append("),");
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(")");

        return new ParameterizedString(sql.toString(), params);
    }

    public InsertResult execute() {
        ParameterizedString query = build();
        PreparedStatement statement = database.createUpdateStatement(query.getSql());
        query.apply(statement, 1);

        try {
            statement.executeUpdate();
            return new InsertResult(statement.getGeneratedKeys(), statement);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
