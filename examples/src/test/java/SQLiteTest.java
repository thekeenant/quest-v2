import net.avicus.quest.Row;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.SQLiteUrl;
import net.avicus.quest.query.insert.Insertion;
import net.avicus.quest.query.select.Select;
import net.avicus.quest.Column;
import org.junit.Test;

public class SQLiteTest {
    @Test
    public void db() {
        Database db = new Database(SQLiteUrl.of("example.db"));
        db.open();

        db.rawUpdate("DROP TABLE IF EXISTS users");
        db.rawUpdate("CREATE TABLE users (id int, name string, age int, quality string)");

        Insertion insertion = Insertion.builder().value("id", 1).value("age", 19).value("name", "Keenan").value("quality", "WISE").build();
        db.insert("users").insert(insertion).execute();

        insertion = Insertion.builder().value("id", 2).value("age", 23).value("name", "Adam").value("quality", "WISE").build();
        db.insert("users").insert(insertion).execute();

        Users users = new Users(db);

        int agesSum = users.ages().sum()

        System.out.println(users.ages().sum());

    }

    private String rowMapper(Row user) {
        return user.get("name").asRequiredString() + " (age " + user.get("age").asRequiredObject() + ")";
    }
}
