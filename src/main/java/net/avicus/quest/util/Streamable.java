package net.avicus.quest.util;

import java.util.stream.Stream;

/**
 * Something that can be streamed.
 * @param <T> The stream type.
 */
public interface Streamable<T> {
    Stream<T> stream();
}
