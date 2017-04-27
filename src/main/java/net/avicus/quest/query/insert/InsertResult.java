package net.avicus.quest.query.insert;

import net.avicus.quest.Record;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class InsertResult {
    private final int result;
    private final Record generated;

    public InsertResult(int result, Record generated) {
        this.result = result;
        this.generated = generated;
    }

    public int getResult() {
        return result;
    }

    public Optional<Record> getGenerated() {
        return Optional.ofNullable(this.generated);
    }

    public static InsertResult execute(PreparedStatement statement) throws DatabaseException {
        try {
            int result = statement.executeUpdate();
            Record generated = null;
            ResultSet set = statement.getGeneratedKeys();
            if (set != null) {
                set.next();
                // Todo:
//                generated = Optional.of(Record.fromResultSet(set));
            }
            return new InsertResult(result, generated);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
