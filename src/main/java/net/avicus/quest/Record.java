package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.util.Primitives;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public abstract class Record {
    /**
     * Used to check if this record has a field.
     * @param field The field index, (1 = first).
     * @return True if the record exists.
     */
    protected abstract boolean hasField(int field);

    /**
     * @param field The field index, (1 = first).
     * @return True if the record exists, false if otherwise.
     * @throws java.util.NoSuchElementException If the field does not exist.
     */
    protected abstract Object getField(int field);

    /**
     * @param label The label.
     * @return The field index of the given label.
     * @throws java.util.NoSuchElementException If the field does not exist.
     */
    protected abstract int getFieldIndex(String label);

    public <T> T map(Function<? super Record, ? extends T> mapper) {
        return mapper.apply(this);
    }

    public <T> Optional<T> get(Column<T> column) {
        return column.map(this);
    }

    public <T> T getNonNull(Column<T> column) {
        return column.mapNonNull(this);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(int field, Class<T> cast) {
        Object data = getField(field);

        if (data == null)
            return Optional.empty();

        // this is safe, trust me
        if (cast.isPrimitive()) {
            cast = (Class<T>) Primitives.toWrapper(cast);
        }

        if (cast.isInstance(data)) {
            T result = (T) data;
            return Optional.of(result);
        }
        else {
            throw new DatabaseException("Couldn't force record field type from " + data.getClass() + " to " + cast);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getNonNull(int field, Class<T> cast) {
        Object data = getField(field);
        if (data == null) {
            throw new DatabaseException("Unexpected null value");
        }

        // this is safe, trust me
        if (cast.isPrimitive()) {
            cast = (Class<T>) Primitives.toWrapper(cast);
        }

        if (cast.isInstance(data)) {
            return (T) data;
        }
        else {
            throw new DatabaseException("Couldn't force record field type from " + data.getClass() + " to " + cast);
        }
    }

    public Optional<Object> get(int field) {
        return get(field, Object.class);
    }

    public Optional<Object> get(String label) {
        return get(getFieldIndex(label), Object.class);
    }

    public Object getNonNull(int field) {
        return getNonNull(field, Object.class);
    }

    public Object getNonNull(String label) {
        return getNonNull(getFieldIndex(label), Object.class);
    }

    public Optional<String> getString(int field) {
        return get(field, String.class);
    }

    public Optional<String> getString(String label) {
        return get(getFieldIndex(label), String.class);
    }

    public String getNonNullString(int field) {
        return getNonNull(field, String.class);
    }

    public String getNonNullString(String label) {
        return getNonNull(getFieldIndex(label), String.class);
    }

    public Optional<Date> getDate(int field) {
        return get(field, Date.class);
    }

    public Optional<Date> getDate(String label) {
        return get(getFieldIndex(label), Date.class);
    }

    public Date getNonNullDate(int field) {
        return getNonNull(field, Date.class);
    }

    public Date getNonNullDate(String label) {
        return getNonNull(getFieldIndex(label), Date.class);
    }

    public Optional<Time> getTime(int field) {
        return get(field, Time.class);
    }

    public Optional<Time> getTime(String label) {
        return get(getFieldIndex(label), Time.class);
    }

    public Time getNonNullTime(int field) {
        return getNonNull(field, Time.class);
    }

    public Time getNonNullTime(String label) {
        return getNonNull(getFieldIndex(label), Time.class);
    }

    public Optional<Timestamp> getTimestamp(int field) {
        return get(field, Timestamp.class);
    }

    public Optional<Timestamp> getTimestamp(String label) {
        return get(getFieldIndex(label), Timestamp.class);
    }

    public Timestamp getNonNullTimestamp(int field) {
        return getNonNull(field, Timestamp.class);
    }

    public Timestamp getNonNullTimestamp(String label) {
        return getNonNull(getFieldIndex(label), Timestamp.class);
    }

    public Optional<Boolean> getBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return get(field).map(data -> Objects.equals(data, true));
    }

    public Optional<Boolean> getBoolean(String label) {
        // Todo: Accept 1 get true get well?
        return get(getFieldIndex(label)).map(data -> Objects.equals(data, true));
    }

    public boolean getNonNullBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return Objects.equals((getNonNull(field)), true);
    }

    public boolean getNonNullBoolean(String label) {
        // Todo: Accept 1 get true get well?
        return Objects.equals((getNonNull(getFieldIndex(label))), true);
    }

    public Optional<Integer> getInt(int field) {
        return get(field, Integer.class);
    }

    public Optional<Integer> getInt(String label) {
        return get(getFieldIndex(label), Integer.class);
    }

    public int getNonNullInt(int field) {
        return getNonNull(field, Integer.class);
    }

    public int getNonNullInt(String label) {
        return getNonNull(getFieldIndex(label), Integer.class);
    }

    public Optional<Long> getLong(int field) {
        return get(field, Long.class);
    }

    public Optional<Long> getLong(String label) {
        return get(getFieldIndex(label), Long.class);
    }

    public long getNonNullLong(int field) {
        return getNonNull(field, Long.class);
    }

    public long getNonNullLong(String label) {
        return getNonNull(getFieldIndex(label), Long.class);
    }

    public Optional<Float> getFloat(int field) {
        return get(field, Float.class);
    }

    public Optional<Float> getFloat(String label) {
        return get(getFieldIndex(label), Float.class);
    }

    public float getNonNullFloat(int field) {
        return getNonNull(field, Float.class);
    }

    public float getNonNullFloat(String label) {
        return getNonNull(getFieldIndex(label), Float.class);
    }

    public Optional<Double> getDouble(int field) {
        return get(field, Double.class);
    }

    public Optional<Double> getDouble(String label) {
        return get(getFieldIndex(label), Double.class);
    }

    public double getNonNullDouble(int field) {
        return getNonNull(field, Double.class);
    }

    public double getNonNullDouble(String label) {
        return getNonNull(getFieldIndex(label), Double.class);
    }

    public Optional<Short> getShort(int field) {
        return get(field, Short.class);
    }

    public Optional<Short> getShort(String label) {
        return get(getFieldIndex(label), Short.class);
    }

    public short getNonNullShort(int field) {
        return getNonNull(field, Short.class);
    }

    public short getNonNullShort(String label) {
        return getNonNull(getFieldIndex(label), Short.class);
    }

    public Optional<Byte> getByte(int field) {
        return get(field, Byte.class);
    }

    public Optional<Byte> getByte(String label) {
        return get(getFieldIndex(label), Byte.class);
    }

    public byte getNonNullByte(int field) {
        return getNonNull(field, Byte.class);
    }

    public byte getNonNullByte(String label) {
        return getNonNull(getFieldIndex(label), Byte.class);
    }

    public Optional<BigDecimal> getBigDecimal(int field) {
        return get(field, BigDecimal.class);
    }

    public Optional<BigDecimal> getBigDecimal(String label) {
        return get(getFieldIndex(label), BigDecimal.class);
    }

    public BigDecimal getNonNullBigDecimal(int field) {
        return getNonNull(field, BigDecimal.class);
    }

    public BigDecimal getNonNullBigDecimal(String label) {
        return getNonNull(getFieldIndex(label), BigDecimal.class);
    }

    public Optional<Number> getNum(int field) {
        return get(field, Number.class);
    }

    public Optional<Number> getNum(String label) {
        return get(getFieldIndex(label), Number.class);
    }

    public Number getNonNullNum(int field) {
        return getNonNull(field, Number.class);
    }

    public Number getNonNullNum(String label) {
        return getNonNull(getFieldIndex(label), Number.class);
    }

}
