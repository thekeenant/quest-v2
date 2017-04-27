package net.avicus.quest.query.select;

import net.avicus.quest.Record;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.util.Streamable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Lazily retrieve records from a {@link ResultSet}.
 *
 * It should be manually closed after usage with {@link Cursor#close()} or automatically
 * closed by using try-with-resources.
 */

public class Cursor extends Record implements Iterable<Cursor>, Streamable<Cursor>, AutoCloseable {
    private final Statement statement;
    private final ResultSet resultSet;

    private boolean started;
    private boolean invalidated;

    public Cursor(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
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
        try {
            return resultSet.getObject(field);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected int getFieldIndex(String label) {
        try {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String curr = resultSet.getMetaData().getColumnLabel(i);
                if (label.equals(curr)) {
                    return i;
                }
            }
            throw new IllegalArgumentException("Label \"" + label + "\" not found");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Advance the cursor by one.
     * @return If a record exists after advancing the cursor.
     */
    public boolean next() {
        return next(false);
    }

    /**
     * Advance the cursor by one.
     * @param ignoreInvalid When true, no exception will be thrown if this cursor is invalidated.
     * @return If a record exists after advancing the cursor.
     */
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

    /**
     * Equivalent to {@link #next()}, but it returns the cursor.
     * @return The cursor.
     */
    public Cursor moveNext() {
        if (!next()) {
            throw new NoSuchElementException();
        }
        return this;
    }

    /**
     * Create a stream from the {@link #iterator()}.
     *
     * This invalidates other usage of this instance of the {@link Cursor}.
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
     * Create an iterator to read records from the the result set..
     *
     * This invalidates other usage of this instance of the {@link Cursor}.
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
    private void ensureValid() {
        if (invalidated) {
            throw new DatabaseException("Cursor was delegated to a different interface, and thus is invalidated");
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
     * Iterates the cursor by advancing it each time {@link #hasNext()} is called. Records
     * are lost if {@link #hasNext()} is called more once before {@link #next()}
     * is retrieved, because of the way streaming result sets work.
     */
    private class CursorIterator implements Iterator<Cursor> {
        private boolean hasNext;
        private boolean nextUsed = true;

        private CursorIterator() {
            invalidate();
        }

        @Override
        public boolean hasNext() {
            if (!nextUsed) {
                throw new IllegalStateException("next() should be called following every hasNext() call");
            }
            hasNext = Cursor.this.next(true);
            nextUsed = false;
            return hasNext;
        }

        @Override
        public Cursor next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            nextUsed = true;
            return Cursor.this;
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
}
