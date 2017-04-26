package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.select.SelectResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A row from a result set. ResultSet's are silly because they
 * go away. We create a row to keep data/metadata from a single row.
 */
public class Record {
    private final List<String> columnNames;
    private final List<RecordField> values;

    public Record(List<String> columnNames, List<RecordField> values) {
        this.columnNames = columnNames;
        this.values = values;
    }

    public <T> T map(Function<Record, ? extends T> mapper) {
        return mapper.apply(this);
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> values = new HashMap<>();
        for (int i = 0; i < this.columnNames.size(); i++) {
            values.put(this.columnNames.get(i), this.values.get(i).asObject().orElse(null));
        }
        return values;
    }

    public boolean hasColumn(int number) {
        int index = number - 1;
        return index >= 0 && index < this.values.size();
    }

    public boolean hasColumn(String column) {
        int number = this.columnNames.indexOf(column);
        return hasColumn(number);
    }

    public String getColumnName(int number) {
        int index = number - 1;
        if (index < 0 || index >= this.columnNames.size()) {
            throw new IllegalArgumentException("Column number not present: " + number);
        }
        return this.columnNames.get(index);
    }

    public int getColumnCount() {
        return this.columnNames.size();
    }

    public RecordField field(int number) throws DatabaseException {
        int index = number - 1;
        if (!hasColumn(number)) {
            throw new IllegalArgumentException("Column number not present: " + number);
        }
        return this.values.get(index);
    }

    public RecordField field(String column) {
        int number = this.columnNames.indexOf(column) + 1;
        if (number == 0) {
            throw new IllegalArgumentException("Column name not present: " + column);
        }
        return field(number);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Record(");
        for (int i = 0; i < this.columnNames.size(); i++) {
            sb.append(this.columnNames.get(i));
            sb.append("[").append(i + 1).append("]");
            sb.append("=");
            sb.append(this.values.get(i));
            if (i != this.columnNames.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static Record fromSelectResultSet(SelectResult result, ResultSet set) {
        List<RecordField> values = new ArrayList<>();
        for (int i = 1; i <= result.getColumnCount(); i++) {
            try {
                values.add(new RecordField(set.getObject(i)));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return new Record(result.getColumnNames(), values);
    }

    public static Record fromResultSet(ResultSet set) {
        List<String> columnNames = new ArrayList<>();
        List<RecordField> values = new ArrayList<>();

        try {
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                columnNames.add(set.getMetaData().getColumnName(i));
                values.add(new RecordField(set.getObject(i)));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new Record(columnNames, values);
    }

    public static Record fromRowValues(Map<String, RecordField> data) {
        return new Record(new ArrayList<>(data.keySet()), new ArrayList<>(data.values()));
    }

    public static Record fromObjects(Map<String, Object> data) {
        List<RecordField> values = data.values().stream().map(RecordField::new).collect(Collectors.toList());
        return new Record(new ArrayList<>(data.keySet()), values);
    }

    public static RecordBuilder builder() {
        return new RecordBuilder();
    }

    public static class RecordBuilder {
        private final Map<String, RecordField> values;

        private RecordBuilder() {
            this.values = new HashMap<>();
        }

        public <T> RecordBuilder with(Column<T> column, T value) {
            return with(column.getName(), value);
        }

        public <I, T> RecordBuilder with(MappedColumn<I, T> column, T value) {
            return with(column.getName(), column.fromMappedType(value));
        }

        public RecordBuilder with(String column, Object value) {
            this.values.put(column, new RecordField(value));
            return this;
        }

        public RecordBuilder with(Map<String, Object> values) {
            for (Entry<String, Object> entry : values.entrySet()) {
                this.values.put(entry.getKey(), new RecordField(entry.getValue()));
            }
            return this;
        }

        public Record build() {
            return new Record(new ArrayList<>(this.values.keySet()), new ArrayList<>(this.values.values()));
        }
    }
}
