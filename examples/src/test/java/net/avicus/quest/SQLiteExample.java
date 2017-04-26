package net.avicus.quest;

import net.avicus.quest.Users.Quality;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.SQLiteUrl;
import net.avicus.quest.parameter.WildcardParam;
import org.junit.Test;

public class SQLiteExample {
    @Test
    public void db() {
        Database db = new Database(SQLiteUrl.of("example.db"));
        db.open();

        db.rawUpdate("DROP TABLE IF EXISTS users");
        db.rawUpdate("CREATE TABLE users (id int, name string, age int, quality string)");

        Users users = new Users(db);

        Row keenan = Row.builder()
                .with(Users.id, 1)
                .with(Users.age, 19)
                .with(Users.quality, Quality.WISE)
                .build();

        Row adam = Row.builder()
                .with(Users.id, 2)
                .with(Users.age, 23)
                .with(Users.quality, Quality.CLEVER)
                .build();

        users.insert(keenan, adam).execute();

        System.out.println(users.all().select(WildcardParam.INSTANCE.sum()));

        Object num = users.all().select(Users.age.sum()).execute().stream(1).findFirst();
        System.out.println(num);
    }

    private String rowMapper(Row user) {
        return user.get("name").asRequiredString() + " (age " + user.get("age").asRequiredObject() + ")";
    }
}
