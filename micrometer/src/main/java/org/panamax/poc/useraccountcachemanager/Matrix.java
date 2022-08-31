package org.panamax.poc.useraccountcachemanager;

import java.util.concurrent.TimeUnit;

public interface Matrix {
    public void record(long duration, TimeUnit timeUnit);
}
