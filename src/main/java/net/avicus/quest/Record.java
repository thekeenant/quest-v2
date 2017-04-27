package net.avicus.quest;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.util.Primitives;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public interface Record {
    boolean hasField(int field);

    Object getField(int field);

    int getFieldIndex(String label);

    @SuppressWarnings("unchecked")
    default <T> Optional<T> get(int field, Class<T> cast) {
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
    default <T> T getNonNull(int field, Class<T> cast) {
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

    default Optional<Object> get(int field) {
        return get(field, Object.class);
    }

    default Optional<Object> get(String label) {
        return get(getFieldIndex(label), Object.class);
    }

    default Object getNonNull(int field) {
        return getNonNull(field, Object.class);
    }

    default Object getNonNull(String label) {
        return getNonNull(getFieldIndex(label), Object.class);
    }

    default Optional<String> getString(int field) {
        return get(field, String.class);
    }

    default String getNonNullString(int field) {
        return getNonNull(field, String.class);
    }

    default Optional<Date> getDate(int field) {
        return get(field, Date.class);
    }

    default Date getNonNullDate(int field) {
        return getNonNull(field, Date.class);
    }

    default Optional<Time> getTime(int field) {
        return get(field, Time.class);
    }

    default Time getNonNullTime(int field) {
        return getNonNull(field, Time.class);
    }

    default Optional<Timestamp> getTimestamp(int field) {
        return get(field, Timestamp.class);
    }

    default Timestamp getNonNullTimestamp(int field) {
        return getNonNull(field, Timestamp.class);
    }

    default Optional<Boolean> getBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return get(field).map(data -> Objects.equals(data, true));
    }

    default boolean getNonNullBoolean(int field) {
        // Todo: Accept 1 get true get well?
        return Objects.equals((getNonNull(field)), true);
    }

    default Optional<Integer> getInt(int field) {
        return get(field, Integer.class);
    }

    default int getNonNullInt(int field) {
        return getNonNull(field, Integer.class);
    }

    default Optional<Long> getLong(int field) {
        return get(field, Long.class);
    }

    default long getNonNullLong(int field) {
        return getNonNull(field, Long.class);
    }

    default Optional<Float> getFloat(int field) {
        return get(field, Float.class);
    }

    default float getNonNullFloat(int field) {
        return getNonNull(field, Float.class);
    }

    default Optional<Double> getDouble(int field) {
        return get(field, Double.class);
    }

    default double getNonNullDouble(int field) {
        return getNonNull(field, Double.class);
    }

    default Optional<Short> getShort(int field) {
        return get(field, Short.class);
    }

    default short getNonNullShort(int field) {
        return getNonNull(field, Short.class);
    }

    default Optional<Byte> getByte(int field) {
        return get(field, Byte.class);
    }

    default byte getNonNullByte(int field) {
        return getNonNull(field, Byte.class);
    }

    default Optional<BigDecimal> getBigDecimal(int field) {
        return get(field, BigDecimal.class);
    }

    default BigDecimal getNonNullBigDecimal(int field) {
        return getNonNull(field, BigDecimal.class);
    }

    default Optional<Number> getNum(int field) {
        return get(field, Number.class);
    }

    default Number getNonNullNum(int field) {
        return getNonNull(field, Number.class);
    }

}
