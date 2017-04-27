package net.avicus.quest.query.select;

import net.avicus.quest.Record;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.util.Streamable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;

public class SelectResult implements Iterable<Record>, Streamable<Record>, AutoCloseable {
    private final Statement statement;
    private final ResultSet resultSet;
    private String[] labels;
    private Queue<Record> records;

    public SelectResult(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    void populate() {
        try {
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Save labels once
            labels = new String[columnCount];
            for (int i = 0; i <= columnCount; i++) {
                labels[i] = resultSet.getMetaData().getColumnLabel(i + 1);
            }

            // Store all records
            records = new ArrayDeque<>(columnCount);
            while (resultSet.next()) {
                Object[] fields = new Object[columnCount];
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = resultSet.getObject(i + 1);
                }
                records.add(new SavedRecord(fields));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Stream<Record> stream() {
        return records.stream();
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }

    @Override
    public void close() {
        try {
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private class SavedRecord extends Record {
        private final Object[] fields;

        public SavedRecord(Object[] fields) {
            this.fields = fields;
        }

        @Override
        protected boolean hasField(int field) {
            return field >= 1 && field <= fields.length;
        }

        @Override
        protected Object getField(int field) {
            return fields[field - 1];
        }

        @Override
        protected int getFieldIndex(String label) {
            for (int i = 0; i < fields.length; i++) {
                String curr = SelectResult.this.labels[i];
                if (curr.equals(label)) {
                    return i + 1;
                }
            }
            throw new IllegalArgumentException("Label \"" + label + "\" not found");
        }
    }
}
