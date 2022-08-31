package org.panamax.poc.useraccountcachemanager;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MatrixRecorder {
    private final Clock clock;
    private final MatrixProvider provider;
    private Map<MatrixId, Matrix> idToMatrix = new ConcurrentHashMap<>();

    public MatrixRecorder(Clock clock, MatrixProvider provider) {
        this.clock = clock;
        this.provider = provider;
    }

    public <T> T record(MatrixId id, Supplier<T> supplier) {
        final long s = clock.instant().getNano();
        try {
            return supplier.get();
        } finally {
            final long e = clock.instant().getNano();
            idToMatrix.computeIfAbsent(id, mId -> provider.get(id)).record(e-s, TimeUnit.NANOSECONDS);
        }
    }
}
