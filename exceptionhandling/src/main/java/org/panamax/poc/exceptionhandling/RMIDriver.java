package org.panamax.poc.exceptionhandling;

import java.rmi.RemoteException;

public class RMIDriver {
    public Object get() throws RemoteException {
        throw new RemoteException("remote exception thrown");
    }

    public void set(Object o) {
        throw new UnsupportedOperationException("set not supported");
    }
}
