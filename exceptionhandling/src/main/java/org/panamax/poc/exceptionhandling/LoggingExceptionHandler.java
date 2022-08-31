package org.panamax.poc.exceptionhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExceptionHandler implements ExceptionHandler{
    private static final Logger logger = LoggerFactory.getLogger(LoggingExceptionHandler.class);
    public <T> T handle(SupplierWithException<T> o) {
        try {
            return o.get();
        } catch (Exception exception) {

            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T handle(SupplierWithException<T> t, String name) {
        try {
            return t.get();
        } catch (Exception exception) {
            logger.error(String.format("Problem occurred while %s. %s", name, exception.getMessage()), exception);
        }

        return null;
    }

    public void handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
    }
}
