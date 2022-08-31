package org.panamax.poc.exceptionhandling;

@FunctionalInterface
public interface SupplierWithException<T> {
    public T get() throws Exception;
}