package org.panamax.poc.useraccountcachemanager;

@FunctionalInterface
public interface SupplierWithException<T> {
    public T get() throws Exception;
}