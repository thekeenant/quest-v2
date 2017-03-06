package net.avicus.quest.query.update;

import net.avicus.quest.query.QueryResult;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateResult implements QueryResult {
    private final int result;

    public UpdateResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public static UpdateResult execute(PreparedStatement statement) throws DatabaseException {
        try {
            int result = statement.executeUpdate();
            return new UpdateResult(result);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
