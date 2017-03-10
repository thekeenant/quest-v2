import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.database.url.DatabaseUrl;
import net.avicus.quest.query.insert.Insertion;
import net.avicus.quest.query.select.Select;
import net.avicus.quest.table.Column;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteTest {
    @Test
    public void db() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Database db = new Database(new DatabaseUrl() {
            @Override
            public Connection establishConnection() throws DatabaseException {
                Connection conn = null;
                try {
                    conn = DriverManager.getConnection("jdbc:sqlite:example.db");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return conn;
            }
        });

        db.open();

        db.rawUpdate("DROP TABLE IF EXISTS users");
        db.rawUpdate("CREATE TABLE users (id int, name string, age int)");

        Insertion insertion = Insertion.builder().value("id", 1).value("age", 19).value("name", "Keenan").build();
        db.insert("users").insert(insertion).execute();

        insertion = Insertion.builder().value("id", 2).value("age", 23).value("name", "Adam").build();
        db.insert("users").insert(insertion).execute();

        Column<String> name = new Column<>("name");
        Column<String> age = new Column<>("age");

        Select select = db.select("users").where(age.lt(20));

        System.out.println(select.execute().toList());
    }
}
