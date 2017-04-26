package net.avicus.quest.query.insert;

import net.avicus.quest.Record;
import net.avicus.quest.query.QueryResult;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class InsertResult implements QueryResult {
    private final int result;
    private final Optional<Record> generated;

    public InsertResult(int result, Optional<Record> generated) {
        this.result = result;
        this.generated = generated;
    }

    public int getResult() {
        return result;
    }

    public Optional<Record> getGenerated() {
        return this.generated;
    }

    public static InsertResult execute(PreparedStatement statement) throws DatabaseException {
        try {
            int result = statement.executeUpdate();
            Optional<Record> generated = Optional.empty();
            ResultSet set = statement.getGeneratedKeys();
            if (set != null) {
                set.next();
                generated = Optional.of(Record.fromResultSet(set));
            }
            return new InsertResult(result, generated);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
