package net.avicus.quest;

import net.avicus.quest.Users.Quality;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.SQLiteUrl;
import org.junit.Test;

import static net.avicus.quest.Functions.count;

public class SQLiteExample {
    @Test
    public void db() {
        Database db = new Database(SQLiteUrl.of("example.db"));
        db.open();

        db.rawUpdate("DROP TABLE IF EXISTS users");
        db.rawUpdate("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name string, age int, quality string)");

        Users users = new Users(db);

        users.insert("Keenan", 19, Quality.WISE);
        users.insert("Adam", 24, Quality.CLEVER);
        users.insert("Max", 7, Quality.ATHLETIC);
        users.insert("Max", 1, Quality.FUNNY);

        db.insert("users").select(users.all().select(Users.NAME, Users.AGE, Users.QUALITY)).execute();


    }
}
