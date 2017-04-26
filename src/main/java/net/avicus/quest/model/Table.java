package net.avicus.quest.model;

import net.avicus.quest.Row;
import net.avicus.quest.database.Database;

public abstract class Table<M extends Model> {
    private final Database database;
    private final TableSchema schema;

    public Table(Database database, TableSchema schema) {
        this.database = database;
        this.schema = schema;
    }

    public void create() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(schema.getName().getParamString());
        sql.append(" (");
        schema.getColumns().forEach(def -> {
            sql.append(def.getParamString());
            
        });
    }

    public abstract M map(Row row);
}
