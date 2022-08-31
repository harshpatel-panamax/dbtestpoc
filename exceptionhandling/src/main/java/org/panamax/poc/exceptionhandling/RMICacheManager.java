package org.panamax.poc.exceptionhandling;

import java.rmi.RemoteException;

public class RMICacheManager implements CacheManager{
    private final RMIDriver rmiDriver;
    private final RetryExceptionHandler handler;

    public RMICacheManager(RMIDriver rmiDriver, RetryExceptionHandler handler) {
        this.rmiDriver = rmiDriver;
        this.handler = handler;
    }

    @Override
    public Object get() {
        return handler.handle(this::getVal);
    }

    private Object getVal() throws RemoteException {
        return rmiDriver.get();
    }

    @Override
    public void set(Object o) {
        rmiDriver.set(o);
    }
}
