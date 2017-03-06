# Quest
A stupid simple MySQL database API. [Activerecord](https://github.com/rails/rails/tree/master/activerecord) users should feel somewhat at home.

**Connect:**
```java

DatabaseConfig.Builder builder = DatabaseConfig.builder("localhost", "database", "username", "password");
builder.reconnect(true);

DatabaseConfig config = builder.build();

Database database = new Database(config);
database.connect(); // may throw DatabassException
```

**Models:**

```java
public class User extends Model {
    @Id
    @Column
    private int id;

    @Column(unique = true)
    String name;

    @Column(name = "dob")
    Date dateOfBirth;

    public User() {
        // this constructor is always needed
    }

    public User(String name, Date dateOfBirth) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }
}
```

```java
Table<User> users = new Table<>(database, "users", User.class);
users.create(); // create table

User user = new User("thekeenant", new Date()));
users.insert(user);
System.out.println(user.id); // auto generated id

User keenan = users.select().where("id", user.id).execute().first();
users.delete().where("id", keenan.id).execute(); // bye bye
```

**Standard, model-less queries:**

```java
// Raw-er queries
Select select = database.select("users").where("id", 50, Operator.LESS_OR_EQUAL);

RowIterator iterator = select.executeIterator();
while (iterator.hasNext())
    System.out.println(iterator.next());

RowList list = select.execute();
System.out.println(list);

// Other
database.update("users").set("name", "bill").where("id", 13).execute();
database.delete("users").where("name", "bill").execute();

// WHERE ((id < 10 OR id = 25) AND age > 18)
Filter filter = new Filter("id", 10, Operator.LESS).or("id", 25);
filter = new Filter("age", 18, Operator.GREATER_OR_EQUAL).and(filter);

database.update("users").where(filter).set("coolness", 15).execute();
```

**Disconnect:**
```java
// Disconnect
database.disconnect();
```
