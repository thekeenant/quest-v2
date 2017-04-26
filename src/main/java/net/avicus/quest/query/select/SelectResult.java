package net.avicus.quest.query.select;

import net.avicus.quest.Row;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.QueryResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SelectResult implements QueryResult, Iterable<Row> {
    private final ResultSet set;
    private final List<String> columns;
    private boolean started;
    private Row current;

    public SelectResult(ResultSet set, List<String> columns) {
        this.set = set;
        this.columns = columns;
    }

    public int getColumnNumber(String columnName) {
        if (!this.columns.contains(columnName)) {
            throw new DatabaseException("Column not present: " + columnName + ".");
        }

        return this.columns.indexOf(columnName) + 1;
    }

    public int getColumnCount() {
        return this.columns.size();
    }

    public List<String> getColumnNames() {
        return this.columns;
    }

    public boolean isStarted() {
        return this.started;
    }

    public boolean next() throws DatabaseException {
        try {
            this.started = true;
            boolean next = this.set.next();
            this.current = next ? Row.fromSelectResultSet(this, this.set) : null;
            return next;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Row getCurrent() {
        if (this.current == null) {
            throw new DatabaseException("Now row is present.");
        }
        return this.current;
    }

    public List<Row> toList() {
        checkNotStarted();

        List<Row> rows = new ArrayList<>();
        while (next()) {
            rows.add(getCurrent());
        }

        return rows;
    }

    private void checkNotStarted() {
        if (isStarted()) {
            throw new DatabaseException("Select was already started.");
        }
    }

    public Stream<Row> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }

    @Override
    public Iterator<Row> iterator() throws DatabaseException {
        checkNotStarted();
        try {
            boolean hasNext = set.first();
            if (!hasNext) {
                return Collections.emptyIterator();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Iterator not supported for streaming query", e);
        }
        return new SelectIterator();
    }

    public class SelectIterator implements Iterator<Row> {
        private boolean hasNext = true;

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Row next() {
            hasNext = SelectResult.this.next();
            return current;
        }
    }

    public static SelectResult execute(PreparedStatement statement) throws DatabaseException {
        try {
            ResultSet set = statement.executeQuery();
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                columns.add(set.getMetaData().getColumnName(i));
            }
            return new SelectResult(set, columns);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
