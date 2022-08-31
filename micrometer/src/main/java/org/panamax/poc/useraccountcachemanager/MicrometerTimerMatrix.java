package org.panamax.poc.useraccountcachemanager;

import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

public class MicrometerTimerMatrix implements Matrix{
    private final Timer timer;

    public MicrometerTimerMatrix(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void record(long duration, TimeUnit timeUnit) {
        timer.record(duration, timeUnit);
    }
}
