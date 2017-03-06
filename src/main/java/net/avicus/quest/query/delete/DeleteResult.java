package net.avicus.quest.query.delete;

import net.avicus.quest.query.QueryResult;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteResult implements QueryResult {
    private final int result;

    public DeleteResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public static DeleteResult execute(PreparedStatement statement) throws DatabaseException {
        try {
            int result = statement.executeUpdate();
            return new DeleteResult(result);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
