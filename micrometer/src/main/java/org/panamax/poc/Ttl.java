package org.panamax.poc;

import java.time.Duration;
import java.util.function.Supplier;

public interface Ttl extends Supplier<Duration> {
    static Ttl of(Duration duration) {
        return () -> duration;
    }
}
