package net.avicus.quest.query.select;

import net.avicus.quest.Record;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.util.Streamable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Retrieve records from a {@link ResultSet}, one-by-one.
 *
 * It should be manually closed after usage with {@link Cursor#close()} or automatically
 * closed by using try-with-resources.
 *
 * Note: This is not the typical usage of {@link Iterable}... The the iterator points to itself. Upon
 * each call to {@link Iterator#next()}, the cursor advances.
 *
 * Note: Only one stream or iterator can be created. Neither can be created after the cursor has
 * been advanced.
 */
public class Cursor extends Record implements Iterable<Cursor>, Streamable<Cursor>, AutoCloseable {
    private final ResultSet resultSet;
    private final Statement statement;

    /**
     * We cache the labels so that repetitive accesses to records via labels is not too costly.
     */
    private Map<String, Integer> labels;

    private boolean started;
    private boolean invalidated;

    public Cursor(ResultSet resultSet, Statement statement) {
        this.resultSet = resultSet;
        this.statement = statement;
    }

    public boolean isClosed() {
        try {
            return resultSet.isClosed();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    protected boolean hasField(int field) {
        try {
            return field >= 1 && field <= resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected Object getField(int field) {
        if (!hasField(field)) {
            throw new NoSuchElementException();
        }

        try {
            return resultSet.getObject(field);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected int getFieldIndex(String label) {
        if (labels != null) {
            Integer index = labels.get(label);

            if (index == null) {
                throw new NoSuchElementException();
            }

            return index;
        }

        try {
            labels = new HashMap<>();

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String curr = resultSet.getMetaData().getColumnLabel(i);
                if (label.equals(curr)) {
                    labels.put(curr, i);
                }
            }
            return getFieldIndex(label);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Advance the cursor by one.
     * @return If a record exists after advancing the cursor.
     */
    public boolean moveNext() {
        return moveNext(false);
    }

    /**
     * Advance the cursor by one.
     * @param ignoreInvalid When true, no exception will be thrown if this cursor is invalidated.
     * @return If a record exists after advancing the cursor.
     */
    private boolean moveNext(boolean ignoreInvalid) {
        if (!ignoreInvalid) {
            ensureValid();
        }

        if (isClosed()) {
            throw new DatabaseException("Cursor result set has been closed");
        }

        started = true;

        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Advance the cursor by one.
     * @return The cursor itself.
     */
    public Cursor next() {
        if (!moveNext()) {
            throw new NoSuchElementException();
        }
        return this;
    }

    /**
     * Create a stream from the {@link #iterator()}.
     *
     * @return The new stream.
     */
    public Stream<Cursor> stream() {
        Stream<Cursor> stream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED | Spliterator.NONNULL),
                false);
        return stream.onClose(this::close);
    }

    /**
     * Create an iterator to read records from the the result set.
     *
     * @return The new iterator.
     */
    @Override
    public Iterator<Cursor> iterator() {
        ensureValid();
        ensureNotStarted();
        return new CursorIterator();
    }

    /**
     * Close the underlying result set and statement in order to safely indicate
     * they are no longer used objects, making the GC happy.
     */
    @Override
    public void close() {
        try {
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Invalidate the cursor.
     */
    private void invalidate() {
        invalidated = true;
    }

    /**
     * @throws DatabaseException If the cursor has been invalidated.
     */
    protected void ensureValid() {
        if (invalidated) {
            throw new DatabaseException("Cursor was delegated to a stream or iterator, and thus is invalidated");
        }
    }

    /**
     * @throws DatabaseException If the cursor has been advanced (started).
     */
    private void ensureNotStarted() {
        if (started) {
            throw new DatabaseException("Cursor not at start, it cannot be iterated upon");
        }
    }

    @Override
    public String toString() {
        return "Cursor(started=" + started + ", invalidated=" + invalidated + ")";
    }

    /**
     * Iterates the cursor by advancing it each time {@link #next()} is called.
     */
    private class CursorIterator implements Iterator<Cursor> {
        /**
         * Indicates if there exists a record which {@link #next()} will return.
         */
        private boolean hasNext;

        /**
         * Indicates if {@link #next()} has been called already on the current record.
         */
        private boolean nextUsed = true;

        private CursorIterator() {
            invalidate();
        }

        @Override
        public boolean hasNext() {
            // Condition passes if hasNext() returned true, and a new record was found, and next() hasn't been called
            if (!nextUsed) {
                // Therefore, next() points to a record so hasNext() returns true.
                return true;
            }

            // Advance the cursor and store whether or not the next record exists
            hasNext = Cursor.this.moveNext(true);

            // If it does, indicate that next() hasn't been called, and should be called, in
            // order to advance the cursor to the next record.
            if (hasNext) {
                nextUsed = false;
            }

            return hasNext;
        }

        @Override
        public Cursor next() {
            // next() was already called once on this record, advance the record
            if (nextUsed) {
                hasNext = hasNext();
            }

            // Check if we are all done
            if (!hasNext) {
                throw new NoSuchElementException();
            }

            // indicate that next() has been called on this record
            nextUsed = true;

            return Cursor.this;
        }
    }
}
