# Quest
A simple SQL database library for Java that takes advantage of the awesome
functional features found in Java 8. It is designed to be:

* **Efficient**: Quest is a light-weight abstraction of JDBC that isn't too complex.
* **Concise**: With a single line you can perform complex operations on a database, taking
  advantage of Java 8's `Stream`, and functional programming features.
* **Powerful**: Quest doesn't limit you, you can still do anything you want with your database,
  even if that means writing raw SQL queries!
* **Familiar**: Operations in Java using Quest resemble SQL to some degree.
* **Safe**: It is easy to make Quest do precisely what you want. Any exceptions you may
  encounter with Quest are designed to be easily handled and resolved.

With Quest you can...
* Select, insert, delete and update records in SQL databases (SQLite, MySQL tested).
* Define a schema which can be used to seamlessly manipulate and access database fields.
* Stream data straight from the database, mapped to a custom model data type.
* Execute raw SQL queries if you wish.
* And much more!

Quest is very young and in "alpha" I suppose. It still lacks documentation and features
will break upon every version iteration.

## Examples

Example model:

```java
import net.avicus.quest.Column;
import net.avicus.quest.database.Database;
import net.avicus.quest.query.select.Select;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class net.avicus.quest.Users {
    // Schema at the top
    private static final Column<Integer> id = Column.of("id");
    private static final Column<String> name = Column.of("name");
    private static final Column<Integer> age = Column.of("age");

    // Maps from a string in the database to a Java enum
    private static final Column<Quality> quality =
            Column.<String, Quality>of("quality", (record, str) -> Quality.valueOf(str));

    enum Quality {
        WISE,
        CLEVER,
        ATHLETIC,
        FUNNY;
    }


    private final Database database;

    public net.avicus.quest.Users(Database database) {
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

Elsewhere, if you have a `users` variable...

```java
// sum of all ages
int sum = users.ages().sum();

// print all Quality's
users.qualities().forEach(System.out::println);
```