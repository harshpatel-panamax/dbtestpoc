package org.panamax.poc.exceptionhandling;

public class RemoteDriver {
    public Object get() {
        throw new UnsupportedOperationException("get not supported");
    }

    public void set(Object o) {
        throw new UnsupportedOperationException("set not supported");
    }
}
