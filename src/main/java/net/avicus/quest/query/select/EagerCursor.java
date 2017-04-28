package net.avicus.quest.query.select;

import net.avicus.quest.database.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EagerCursor extends Cursor {
    public EagerCursor(ResultSet resultSet, Statement statement) {
        super(resultSet, statement);
    }

    /**
     * Move to a specific row number.
     *
     * @param rowNumber The row number, (1 = first, -1 = last).
     * @throws DatabaseException If an error occurs, or the operation is not supported.
     */
    public void moveTo(int rowNumber) {
        try {
            getResultSet().absolute(rowNumber);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Move to a specific row number.
     *
     * @param rowNumber The row number, (1 = first, -1 = last).
     * @return The cursor instance itself.
     * @throws DatabaseException If an error occurs, or the operation is not supported.
     */
    public EagerCursor move(int rowNumber) {
        moveTo(rowNumber);
        return this;
    }

    /**
     * Move to the first record.
     *
     * @throws DatabaseException If an error occurs, or the operation is not supported.
     */
    public void moveToFirst() {
        try {
            getResultSet().first();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Move to the first record.
     *
     * @return The cursor instance itself.
     * @throws DatabaseException If an error occurs, or the operation is not supported.
     */
    public EagerCursor first() {
        moveToFirst();
        return this;
    }

    /**
     * Move to the last record.
     *
     * @throws DatabaseException If an error occurs, or the operation is not supported.
     */
    public void moveToLast() {
        try {
            getResultSet().last();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Move to the last record.
     *
     * @return The cursor instance itself.
     * @throws DatabaseException If an error occurs, or the operation is not supported.
     */
    public EagerCursor last() {
        moveToLast();
        return this;
    }

    @Override
    protected void ensureValid() {
        // Always valid
    }
}
