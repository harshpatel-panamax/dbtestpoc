package org.panamax.poc.exceptionhandling;

public interface ExceptionHandler {
    <T> T handle(SupplierWithException<T> supplier);

    void handleException(Exception e);

    default <T> T handle(SupplierWithException<T> t, String name) {
        return handle(t);
    }
}
