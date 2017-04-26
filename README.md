# Quest
A stupid simple SQL database API for Java.

Example model:

```java
import net.avicus.quest.Column;
import net.avicus.quest.database.Database;
import net.avicus.quest.query.select.Select;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Users {
    // Schema at the top
    private static final Column<Integer> id = Column.of("id");
    private static final Column<String> name = Column.of("name");
    private static final Column<Integer> age = Column.of("age");

    // Maps from a string in the database to a Java enum
    private static final Column<Quality> quality =
            Column.<String, Quality>of("quality", (row, str) -> Quality.valueOf(str));

    enum Quality {
        WISE,
        CLEVER,
        ATHLETIC,
        FUNNY
    }

    private final Database database;

    public Users(Database database) {
        this.database = database;
    }

    // Generic select * from users statement
    public Select all() {
        return database.select("users");
    }

    // Select all that are older than 70
    public Select elderly() {
        return all().where(age.greaterThan(70));
    }

    // Select only people whose age is even
    public Select evenAges() {
        return all().where(age.mod(2).eq(0));
    }

    // A streaming set of ages for all users
    public IntStream ages() {
        return all().stream().map(age::getRequired).mapToInt(Integer::intValue);
    }
    
    // A streaming set of qualities of all users
    public Stream<Quality> qualities() {
        return all().stream().map(quality::getRequired);
    }
}
```