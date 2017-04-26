package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.select.SelectResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A row from a result set. ResultSet's are silly because they
 * go away. We create a row to keep data/metadata from a single row.
 */
public class Row {
    private final List<String> columnNames;
    private final List<RowField> values;

    public Row(List<String> columnNames, List<RowField> values) {
        this.columnNames = columnNames;
        this.values = values;
    }

    public <T> T map(Function<Row, ? extends T> mapper) {
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
            throw new IllegalArgumentException("Column number not present: " + number + ".");
        }
        return this.columnNames.get(index);
    }

    public int getColumnCount() {
        return this.columnNames.size();
    }

    public RowField get(int number) throws DatabaseException {
        int index = number - 1;
        if (!hasColumn(number)) {
            throw new IllegalArgumentException("Column number not present: " + number + ".");
        }
        return this.values.get(index);
    }

    public RowField get(String column) {
        int number = this.columnNames.indexOf(column) + 1;
        if (number == 0) {
            throw new IllegalArgumentException("Column name not present: " + column + ".");
        }
        return get(number);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRequired(Column<T> column) {
        return column.getRequired(this);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Column<T> column) {
        return column.get(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Row(");
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

    public static Row fromSelectResultSet(SelectResult result, ResultSet set) {
        List<RowField> values = new ArrayList<>();
        for (int i = 1; i <= result.getColumnCount(); i++) {
            try {
                values.add(new RowField(set.getObject(i)));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        return new Row(result.getColumnNames(), values);
    }

    public static Row fromResultSet(ResultSet set) {
        List<String> columnNames = new ArrayList<>();
        List<RowField> values = new ArrayList<>();

        try {
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                columnNames.add(set.getMetaData().getColumnName(i));
                values.add(new RowField(set.getObject(i)));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return new Row(columnNames, values);
    }

    public static Row fromRowValues(Map<String, RowField> data) {
        return new Row(new ArrayList<>(data.keySet()), new ArrayList<>(data.values()));
    }

    public static Row fromObjects(Map<String, Object> data) {
        List<RowField> values = data.values().stream().map(RowField::new).collect(Collectors.toList());
        return new Row(new ArrayList<>(data.keySet()), values);
    }

    public static RowBuilder builder() {
        return new RowBuilder();
    }

    public static class RowBuilder {
        private final Map<String, RowField> values;

        private RowBuilder() {
            this.values = new HashMap<>();
        }

        public <T> RowBuilder with(Column<T> column, T value) {
            return with(column.getName(), value);
        }

        public <I, T> RowBuilder with(MappedColumn<I, T> column, T value) {
            return with(column.getName(), column.fromMappedType(value));
        }

        public RowBuilder with(String column, Object value) {
            this.values.put(column, new RowField(value));
            return this;
        }

        public RowBuilder with(Map<String, Object> values) {
            for (Entry<String, Object> entry : values.entrySet()) {
                this.values.put(entry.getKey(), new RowField(entry.getValue()));
            }
            return this;
        }

        public Row build() {
            return new Row(new ArrayList<>(this.values.keySet()), new ArrayList<>(this.values.values()));
        }
    }
}
