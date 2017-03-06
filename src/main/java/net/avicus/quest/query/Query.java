package net.avicus.quest.query;

import java.util.Optional;

public interface Query<R extends QueryResult, C extends QueryConfig> {
    default R execute(C config) {
        return execute(Optional.of(config));
    }

    default R execute() {
        return execute(Optional.empty());
    }

    R execute(Optional<C> config);
}
