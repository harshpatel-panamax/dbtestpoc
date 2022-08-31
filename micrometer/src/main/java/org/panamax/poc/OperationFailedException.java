package org.panamax.poc;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(Exception exception) {
        super(exception);
    }
}
