package org.panamax.poc.useraccountcachemanager;

import io.micrometer.core.instrument.Counter;

import java.util.concurrent.TimeUnit;

public class MicrometerCounterMatrix implements Matrix{

    private final Counter counter;

    public MicrometerCounterMatrix(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void record(long duration, TimeUnit timeUnit) {
        counter.increment();
    }
}
