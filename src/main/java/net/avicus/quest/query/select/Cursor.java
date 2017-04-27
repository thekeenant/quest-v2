package net.avicus.quest.query.select;

import net.avicus.quest.Record;
import net.avicus.quest.database.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Cursor implements AutoCloseable, Record, Iterable<Record> {
    private final TempRecord tempRecord = new TempRecord();

    private final Statement statement;
    private final ResultSet resultSet;

    private boolean started;
    private boolean invalidated;

    public Cursor(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    @Override
    public void close() {
        System.out.println("Closing...");
        try {
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean hasField(int field) {
        try {
            return field >= 1 && field <= resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Object getField(int field) {
        ensureValid();

        try {
            return resultSet.getObject(field);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getFieldIndex(String label) {
        try {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String curr = resultSet.getMetaData().getColumnLabel(i);
                if (label.equals(curr)) {
                    return i;
                }
            }
            throw new DatabaseException("Label \"" + label + "\" not found");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public boolean next() {
        return next(false);
    }

    private boolean next(boolean ignoreInvalid) {
        if (!ignoreInvalid) {
            ensureValid();
        }

        started = true;

        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Cursor moveNext() {
        if (!next()) {
            throw new NoSuchElementException();
        }
        return this;
    }

    public Stream<Record> stream() {
        Stream<Record> stream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(strictIterator(), Spliterator.ORDERED | Spliterator.NONNULL),
                false);
        return stream.onClose(this::close);
    }

    private Iterator<Record> strictIterator() {
        ensureValid();
        ensureNotStarted();
        return new StrictIterator();
    }

    @Override
    public Iterator<Record> iterator() {
        ensureValid();
        ensureNotStarted();
        return new FlexibleIterator();
    }

    private void invalidate() {
        invalidated = true;
    }

    private void ensureValid() {
        if (invalidated) {
            throw new DatabaseException("Cursor was delegated to a different interface, and thus is invalidated");
        }
    }

    private void ensureNotStarted() {
        if (started) {
            throw new DatabaseException("Cursor not at start, it cannot be iterated upon");
        }
    }

    private Object[] createFieldArray() {
        try {
            Object[] current = new Object[resultSet.getMetaData().getColumnCount()];
            for (int i = 0; i < current.length; i++) {
                current[i] = resultSet.getObject(i + 1);
            }
            return current;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private class StrictIterator implements Iterator<Record> {
        private boolean hasNext;

        public StrictIterator() {
            invalidate();
        }

        @Override
        public boolean hasNext() {
            hasNext = Cursor.this.next(true);
            if (hasNext) {
                tempRecord.fields = createFieldArray();
            }
            return hasNext;
        }

        @Override
        public Record next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            return tempRecord;
        }

        @Override
        public void remove() {
            try {
                resultSet.deleteRow();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    private class FlexibleIterator implements Iterator<Record> {
        private final Queue<Object[]> queue = new ArrayDeque<>();

        public FlexibleIterator() {
            invalidate();
        }

        private void doNext() {
            boolean hasNext = Cursor.this.next(true);

            if (hasNext) {
                queue.add(createFieldArray());
            }
        }

        @Override
        public boolean hasNext() {
            doNext();
            return !queue.isEmpty();
        }

        @Override
        public Record next() {
            doNext();
            tempRecord.fields = queue.remove();
            return tempRecord;
        }
    }

    private class TempRecord implements Record {
        private Object[] fields;

        @Override
        public boolean hasField(int field) {
            return Cursor.this.hasField(field);
        }

        @Override
        public Object getField(int field) {
            return fields[field - 1];
        }

        @Override
        public int getFieldIndex(String label) {
            return Cursor.this.getFieldIndex(label);
        }
    }
}
