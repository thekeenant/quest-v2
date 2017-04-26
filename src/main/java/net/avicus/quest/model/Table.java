package net.avicus.quest.model;

import net.avicus.quest.Param;
import net.avicus.quest.ParameterizedString;
import net.avicus.quest.Record;
import net.avicus.quest.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Table<M extends Model> {
    private final Database database;
    private final TableSchema schema;

    public Table(Database database, TableSchema schema) {
        this.database = database;
        this.schema = schema;
    }

    public void create() {
        ParameterizedString str = buildCreate();
        List<Object> params = str.getParameters().stream().map(Param::getValues).collect(Collectors.toList());
        database.rawUpdate(str.getSql(), params);
    }

    public ParameterizedString buildCreate() {
        StringBuilder sql = new StringBuilder();
        List<Param> params = new ArrayList<>();

        sql.append("CREATE TABLE ");

        sql.append(schema.getName().getParamString());
        params.add(schema.getName());

        sql.append(" (");
        schema.getColumns().forEach(def -> {
            sql.append(def.getParamString());
            sql.append(", ");
            params.add(def);
        });

        sql.deleteCharAt(sql.length() - 1).deleteCharAt(sql.length() - 1);

        return new ParameterizedString(sql.toString(), params);
    }

    public abstract M map(Record record);
}
