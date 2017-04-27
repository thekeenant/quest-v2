package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.util.Primitives;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public abstract class Record {
    protected abstract boolean hasField(int field);

    protected abstract Object getField(int field);

    protected abstract int getFieldIndex(String label);

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

    public String getNonNullString(int field) {
        return getNonNull(field, String.class);
    }

    public Optional<Date> getDate(int field) {
        return get(field, Date.class);
    }

    public Date getNonNullDate(int field) {
        return getNonNull(field, Date.class);
    }

    public Optional<Time> getTime(int field) {
        return get(field, Time.class);
    }

    public Time getNonNullTime(int field) {
        return getNonNull(field, Time.class);
    }

    public Optional<Timestamp> getTimestamp(int field) {
        return get(field, Timestamp.class);
    }

    public Timestamp getNonNullTimestamp(int field) {
        return getNonNull(field, Timestamp.class);
    }

    public Optional<Boolean> getBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return get(field).map(data -> Objects.equals(data, true));
    }

    public boolean getNonNullBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return Objects.equals((getNonNull(field)), true);
    }

    public Optional<Integer> getInt(int field) {
        return get(field, Integer.class);
    }

    public int getNonNullInt(int field) {
        return getNonNull(field, Integer.class);
    }

    public Optional<Long> getLong(int field) {
        return get(field, Long.class);
    }

    public long getNonNullLong(int field) {
        return getNonNull(field, Long.class);
    }

    public Optional<Float> getFloat(int field) {
        return get(field, Float.class);
    }

    public float getNonNullFloat(int field) {
        return getNonNull(field, Float.class);
    }

    public Optional<Double> getDouble(int field) {
        return get(field, Double.class);
    }

    public double getNonNullDouble(int field) {
        return getNonNull(field, Double.class);
    }

    public Optional<Short> getShort(int field) {
        return get(field, Short.class);
    }

    public short getNonNullShort(int field) {
        return getNonNull(field, Short.class);
    }

    public Optional<Byte> getByte(int field) {
        return get(field, Byte.class);
    }

    public byte getNonNullByte(int field) {
        return getNonNull(field, Byte.class);
    }

    public Optional<BigDecimal> getBigDecimal(int field) {
        return get(field, BigDecimal.class);
    }

    public BigDecimal getNonNullBigDecimal(int field) {
        return getNonNull(field, BigDecimal.class);
    }

    public Optional<Number> getNum(int field) {
        return get(field, Number.class);
    }

    public Number getNonNullNum(int field) {
        return getNonNull(field, Number.class);
    }

}
