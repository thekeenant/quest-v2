import net.avicus.quest.Column;
import net.avicus.quest.database.Database;
import net.avicus.quest.query.select.Select;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Users {
    public static final Column<Integer> id = Column.of("id");
    public static final Column<String> name = Column.of("name");
    public static final Column<Integer> age = Column.of("age");

    public static final Column<Quality> quality =
            Column.<String, Quality>of("quality", (row, str) -> Quality.valueOf(str));

    private final Database database;

    enum Quality {
        WISE,
        CLEVER,
        ATHLETIC,
        FUNNY
    }

    public Users(Database database) {
        this.database = database;
    }

    public Select all() {
        return database.select("users");
    }

    public Select elderly() {
        return all().where(age.greaterThan(70));
    }

    public Select evenAges() {
        return all().where(age.mod(2).eq(0));
    }

    public IntStream ages() {
        return all().execute().stream().map(age::getRequired).mapToInt(Integer::intValue);
    }

    public Stream<Quality> qualities() {
        return all().execute().stream().map(quality::getRequired);
    }
}
