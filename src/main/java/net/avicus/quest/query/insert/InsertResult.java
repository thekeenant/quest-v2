package net.avicus.quest.query.insert;

import net.avicus.quest.query.QueryResult;
import net.avicus.quest.Row;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class InsertResult implements QueryResult {
    private final int result;
    private final Optional<Row> generated;

    public InsertResult(int result, Optional<Row> generated) {
        this.result = result;
        this.generated = generated;
    }

    public int getResult() {
        return result;
    }

    public Optional<Row> getGenerated() {
        return this.generated;
    }

    public static InsertResult execute(PreparedStatement statement) throws DatabaseException {
        try {
            int result = statement.executeUpdate();
            Optional<Row> generated = Optional.empty();
            ResultSet set = statement.getGeneratedKeys();
            if (set != null) {
                set.next();
                generated = Optional.of(Row.fromResultSet(set));
            }
            return new InsertResult(result, generated);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
