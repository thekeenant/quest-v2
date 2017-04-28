package net.avicus.quest.query.insert;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.select.EagerCursor;

import java.sql.ResultSet;
import java.sql.Statement;

public class InsertResult implements AutoCloseable {
    private ResultSet resultSet;
    private final Statement statement;

    public InsertResult(ResultSet resultSet, Statement statement) {
        this.resultSet = resultSet;
        this.statement = statement;
    }

    public EagerCursor getGenerated() {
        return new EagerCursor(resultSet, null);
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
}
