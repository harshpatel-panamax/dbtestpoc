package org.panamax.poc.exceptionhandling;

public class RetryExceptionHandler implements ExceptionHandler {
    private final LoggingExceptionHandler loggingExceptionHandler;

    public RetryExceptionHandler(LoggingExceptionHandler loggingExceptionHandler) {
        this.loggingExceptionHandler = loggingExceptionHandler;
    }

    @Override
    public <T> T handle(SupplierWithException<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            loggingExceptionHandler.handleException(e);
            return loggingExceptionHandler.handle(supplier);
        }
    }

    @Override
    public void handleException(Exception e) {
        loggingExceptionHandler.handleException(e);
    }
}
