package org.panamax.poc.exceptionhandling;

public class RemoteCacheManager implements CacheManager{
    private final RemoteDriver remoteDriver;
    private final ExceptionHandler handler;

    public RemoteCacheManager(RemoteDriver remoteDriver, ExceptionHandler handler) {
        this.remoteDriver = remoteDriver;
        this.handler = handler;
    }

    @Override
    public Object get() {
        return handler.handle(this::getVal, "get");
    }

    private Object getVal() {
        remoteDriver.get();
        remoteDriver.set(null);
        remoteDriver.get();
        return null;
    }

    @Override
    public void set(Object o) {
        try {
            remoteDriver.set(o);
        } catch (Exception e) {
            handler.handleException(e);
        }
    }
}
