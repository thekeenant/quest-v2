package net.avicus.quest.query.select;

import net.avicus.quest.Record;
import net.avicus.quest.RecordField;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.QueryResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SelectResult implements QueryResult {
    private final ResultSet set;
    private final List<String> columns;

    private Record current;
    private boolean started;
    private boolean invalidated;

    public SelectResult(ResultSet set, List<String> columns) {
        this.set = set;
        this.columns = columns;
    }

    public int getColumnNumber(String columnName) {
        if (!columns.contains(columnName)) {
            throw new DatabaseException("Column not present: " + columnName);
        }

        return columns.indexOf(columnName) + 1;
    }

    public int getColumnCount() {
        return columns.size();
    }

    public List<String> getColumnNames() {
        return columns;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isInvalidated() {
        return invalidated;
    }

    public boolean next() {
        return next(false);
    }

    private boolean next(boolean ignoreInvalidation) throws DatabaseException {
        if (!ignoreInvalidation) {
            checkNotInvalidated();
        }

        try {
            this.started = true;
            boolean next = this.set.next();
            this.current = next ? Record.fromSelectResultSet(this, this.set) : null;
            return next;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Record getCurrent() {
        checkNotInvalidated();

        if (this.current == null) {
            throw new DatabaseException("Now row is present.");
        }
        return this.current;
    }

    public List<Record> toList() {
        checkNotStarted();

        List<Record> records = new ArrayList<>();
        while (next()) {
            records.add(getCurrent());
        }

        return records;
    }

    private void checkNotStarted() {
        if (isStarted()) {
            throw new DatabaseException("Result was already started.");
        }
    }

    private void checkNotInvalidated() {
        if (isInvalidated()) {
            throw new DatabaseException("Result has been invalidated.");
        }
    }

    public Record fetchOne() {
        return iterator().next();
    }

    public RecordField fetchOne(int field) {
        return iterator(field).next();
    }

    public Stream<RecordField> stream(int field) throws DatabaseException {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(field), Spliterator.ORDERED), false);
    }

    public Stream<Record> stream() throws DatabaseException {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }

    public Iterator<RecordField> iterator(int field) throws DatabaseException {
        return new SelectFieldIterator(field);
    }

    public Iterator<Record> iterator() throws DatabaseException {
        return new SelectIterator();
    }

    private void invalidate() {
        invalidated = true;
    }

    public class SelectFieldIterator implements Iterator<RecordField> {
        private final int field;
        private final SelectIterator iterator;

        public SelectFieldIterator(int field) {
            this.field = field;
            iterator = new SelectIterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public RecordField next() {
            return iterator.next().field(field);
        }
    }

    private class SelectIterator implements Iterator<Record> {
        private final List<Record> backlog = new ArrayList<>(3);

        public SelectIterator() {
            checkNotStarted();
            checkNotInvalidated();
            SelectResult.this.invalidate();
        }

        private void doNext() {
            boolean nextExists = SelectResult.this.next(true);
            if (nextExists) {
                backlog.add(current);
            }
        }

        @Override
        public boolean hasNext() {
            doNext();
            return !backlog.isEmpty();
        }

        @Override
        public Record next() {
            doNext();
            if (backlog.isEmpty()) {
                throw new NoSuchElementException();
            }
            return backlog.remove(0);
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
